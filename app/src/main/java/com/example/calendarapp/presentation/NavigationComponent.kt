package com.example.calendarapp.presentation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.calendarapp.domain.Event
import com.example.calendarapp.domain.ForecastData
import com.example.calendarapp.domain.WeatherData
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import java.time.LocalDateTime

@Composable
fun NavigationComponent(
    navController: NavHostController,
    viewModel: CalendarViewModel,
    lat: Double,
    lon: Double,
    forecastWeatherData: ForecastData?,
    weatherData: WeatherData?,
    lastUpdateTime: LocalDateTime
) {
    NavHost(navController = navController, startDestination = Routes.MonthView.route) {

        // Navigation graph destinations
        composable(Routes.MonthView.route){
            MonthView(navController, viewModel)
        }
        composable(Routes.WeatherForecast.route) {
            WeatherDetailScreen(viewModel, lat, lon, forecastWeatherData, navController)
        }

        composable(Routes.DailyView.route){
            ViewPage(navController, viewModel, lat, lon, forecastWeatherData, weatherData, lastUpdateTime)
        }
        composable(Routes.EditEventView.route + "/{startTime}") { backStackEntry ->
            val startTime = backStackEntry.arguments?.getString("startTime")

            // Observe the LiveData using observeAsState
            val allEvents by viewModel.allEvents.observeAsState()

            var theEvent: Event? = null

            // Check if allEvents is not null before iterating
            allEvents?.forEach { event ->
                if (event.startTime.toString() == startTime) {
                    theEvent = event
                }
            }

            theEvent?.let { EventScreen(it, navController, viewModel) }
        }

        composable(Routes.NewEventView.route + "/{option}"){
                backStackEntry -> val option = backStackEntry.arguments?.getString("option")
            if(option != null){
                NewEventScreen(navController, viewModel, option)
            }
        }
    }

}