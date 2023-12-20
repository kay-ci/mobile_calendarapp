package com.example.calendarapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.calendarapp.R
import com.example.calendarapp.domain.ForecastData
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import kotlin.math.roundToInt


@Composable
fun WeatherDetailScreen(
    viewModel: CalendarViewModel,
    lat: Double,
    lon: Double,
    forecastWeatherData: ForecastData?,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavigationBar(navController)
        //Display the header for the forecast
        DisplayForecastHeader()
        // Rest of the content
        val nextFiveDays = viewModel.getNextFiveDays()
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        )
        {
            Spacer(modifier = Modifier.size(16.dp))
            FiveDaysForecast(nextFiveDays, forecastWeatherData, viewModel)
        }
    }
}

@Composable
fun DisplayForecastHeader(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center){
        val context = LocalContext.current
        Text(
            text = context.resources.getString(R.string.forecastTitle),
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 100.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun FiveDaysForecast(nextFiveDays: List<String>, forecastWeatherData: ForecastData?, viewModel: CalendarViewModel) {
    nextFiveDays.forEach { day ->
        // Each day and its corresponding data are in the same Column
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = day.take(3), // Take the first three letters
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            forecastWeatherData?.list?.forEach { weatherEntry ->
                val dayName: String = viewModel.getDayOfWeek(weatherEntry.dt_txt!!)
                if (day == dayName) {
                    val temperature = weatherEntry.main?.temp
                    val time = weatherEntry.dt_txt.substring(11, 16)
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                    ){
                        Text(
                            text = "$time: ${temperature?.roundToInt()}°C",
                            modifier = Modifier.padding(8.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                        // Display weather icon
                        weatherEntry.weather?.get(0)?.icon.let { iconCode ->
                            val iconUrl = "https://openweathermap.org/img/wn/$iconCode.png"
                            Image(
                                painter = rememberImagePainter(iconUrl),
                                contentDescription = "Weather Icon",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}




