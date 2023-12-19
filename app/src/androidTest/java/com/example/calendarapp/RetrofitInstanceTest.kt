package com.example.calendarapp

import androidx.multidex.BuildConfig
import com.example.calendarapp.domain.RetrofitInstance
import org.junit.Test
import retrofit2.Retrofit

class RetrofitInstanceTest {
    @Test
    fun testRetrofitInstance(){
        val instance: Retrofit = RetrofitInstance.retrofit
        assert(instance.baseUrl().url().toString() == "https://api.openweathermap.org/data/2.5/")
    }
}