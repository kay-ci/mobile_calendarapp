package com.example.calendarapp.domain
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("weather?appid=4cb1c77a59428d71252f0729a674a604&units=metric")
    fun getCurrentWeatherData(
        @Query("lat") latitude: String?,
        @Query("lon") longitude: String?
    ): Call<WeatherData>
}