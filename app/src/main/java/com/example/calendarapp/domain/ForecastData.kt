package com.example.calendarapp.domain


data class ForecastData(
    val list: List<ForecastEntry>
)
data class ForecastEntry(
    val dt_txt: String?,
    val main: ForecastMain?,
    val weather: List<ForecastWeather>?
)

data class ForecastMain(
    val temp: Double?
)

data class ForecastWeather(
    val icon: String?
)
