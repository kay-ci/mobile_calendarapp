package com.example.calendarapp.domain
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    open fun getCurrentWeatherData(
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?,
        @Query("appid") apiKey: String?
    ): Call<WeatherData?>?
}