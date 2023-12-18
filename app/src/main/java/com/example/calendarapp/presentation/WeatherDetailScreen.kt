package com.example.calendarapp.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import kotlin.math.roundToInt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
@Composable
fun WeatherDetailScreen(viewModel: CalendarViewModel, lat: Double, lon: Double) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        val forecastWeatherData by viewModel.weatherDataForecast.observeAsState()
        viewModel.fetchNextWeatherData(lat.toString(), lon.toString())

        val nextFiveDays = viewModel.getNextFiveDays()
        nextFiveDays.forEach { day ->
            // Each day and its corresponding data are in the same Column
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = day.take(3), // Take the first three letters
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                forecastWeatherData?.list?.forEach { weatherEntry ->
                    val dayName: String = viewModel.getDayOfWeek(weatherEntry.dt_txt!!)
                    if (day == dayName) {
                        val temperature = weatherEntry.main?.temp
                        val time = weatherEntry.dt_txt.substring(11, 16)
                        Text(
                            text = " $time : ${temperature?.roundToInt()}°C",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}



