package com.example.calendarapp


import com.example.calendarapp.domain.WeatherService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

class WeatherServiceTest {

    private var server: MockWebServer = MockWebServer()


    private var api: WeatherService = Retrofit.Builder()
        .baseUrl(server.url("https://api.openweathermap.org/data/2.5/"))
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(WeatherService::class.java)


    @Before
    fun setUp() {
    }

    @After
    fun tearDown(){
        server.shutdown()
    }



    @Test
    fun testGetCurrentWeatherData(){
        val lat="45.5397869"
        val lon="-73.6152024"
        val response = MockResponse()
            .setResponseCode(200)
            .setBody("""{
                    "main": {"temp": 25.0},
                    "weather": [{"icon": "01d"}]
                }""")

        server.enqueue(response)
        val actualResponse = api.getCurrentWeatherData(lat, lon).execute()

        // Verify the request
        if (server.requestCount > 0) { // Check if a request is available before calling takeRequest()
            val request = server.takeRequest()
            assertEquals("/weather?appid=4cb1c77a59428d71252f0729a674a604&units=metric&lat=$lat&lon=$lon", request.path)
        }
        assertEquals(200, actualResponse.code())
        assertTrue(actualResponse.isSuccessful)
    }

    @Test
    fun testGetFiveDaysWeather(){
        val lat="45.5397869"
        val lon="-73.6152024"
        val response = MockResponse()
            .setResponseCode(200)
            .setBody("""{
                    "main": {"temp": 25.0},
                    "weather": [{"icon": "01d"}]
                }""")

        server.enqueue(response)
        val actualResponse = api.getFiveDaysWeather(lat, lon).execute()

        // Verify the request
        if (server.requestCount > 0) { // Check if a request is available before calling takeRequest()
            val request = server.takeRequest()
            assertEquals("/forecast?appid=4cb1c77a59428d71252f0729a674a604&units=metric&lat=$lat&lon=$lon", request.path)
        }
        assertEquals(200, actualResponse.code())
        assertTrue(actualResponse.isSuccessful)
    }
}
