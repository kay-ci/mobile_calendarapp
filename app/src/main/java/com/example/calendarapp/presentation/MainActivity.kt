package com.example.calendarapp.presentation

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.location.Geocoder;
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.calendarapp.R
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import com.example.calendarapp.ui.theme.CalendarAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
    }

    private fun getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                val lat = location?.latitude
                val lon = location?.longitude

                setContent {
                    CalendarAppTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            if (lat != null && lon != null) {
                                showHomeView(lat, lon)
                            }
                        }
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            // Check if both permissions are granted
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                // Both permissions granted, you can proceed to get the location
                getCurrentLocation()
            } else {
                // Permissions denied
                showHomeView(0.0, 0.0)
                Toast.makeText(this, getString(R.string.location_permissions_denied), Toast.LENGTH_SHORT).show()
            }
        }

    private fun showHomeView(lat: Double, lon: Double) {
        setContent {
            CalendarAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeView(lat, lon)
                }
            }
        }
    }



    @Composable
    fun HomeView(lat : Double , lon : Double) {
        val owner = LocalViewModelStoreOwner.current
        owner?.let {
            val viewModel: CalendarViewModel = viewModel(
                it,
                "CalenderViewModel",
                ViewModelFactory(
                    LocalContext.current.applicationContext
                            as Application
                )
            )
            val navController = rememberNavController()
            val forecastWeatherData by viewModel.weatherDataForecast.observeAsState()
            val weatherData by viewModel.weatherData.observeAsState()
            var lastUpdateTime by remember { mutableStateOf(LocalDateTime.now()) }
            // Fetch weather data every 10 minutes
            LaunchedEffect(Unit) {
                while (true) {
                    viewModel.fetchWeatherData(lat.toString(), lon.toString())
                    viewModel.fetchNextWeatherData(lat.toString(), lon.toString())
                    lastUpdateTime = LocalDateTime.now()
                    delay(10 * 60 * 100) // Delay for 10 minutes
                }
            }
            NavigationComponent(navController, viewModel, lat, lon, forecastWeatherData, weatherData,lastUpdateTime)
        }
    }

    class ViewModelFactory(val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CalendarViewModel(application) as T
        }
    }
}


