package com.example.calendarapp.data

import com.example.calendarapp.domain.ForecastData
import com.example.calendarapp.domain.RetrofitInstance
import com.example.calendarapp.domain.WeatherData
import retrofit2.Call
import retrofit2.Response

class WeatherRepository
{
    private val weatherService = RetrofitInstance.weatherService

    fun getCurrentWeatherData (latitude : String, longitude : String) : Call<WeatherData> {
        return weatherService.getCurrentWeatherData(latitude,longitude)
    }

    fun getFiveDaysWeather(latitude : String, longitude : String) : Call<ForecastData> {
        return weatherService.getFiveDaysWeather(latitude,longitude)
    }

}