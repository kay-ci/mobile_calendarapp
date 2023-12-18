package com.example.calendarapp.domain

data class WeatherData(
    val main: Main?,
    val weather: List<Weather>?
)

data class Main(
    val temp: Double?
)

data class Weather(
    val icon: String?
)