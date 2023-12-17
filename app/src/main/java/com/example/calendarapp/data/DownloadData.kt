package com.example.calendarapp.data

import android.util.Log
import com.example.calendarapp.domain.UtilityHelper
import java.net.HttpURLConnection
import java.net.URL

class DownloadData (utilityHelper: UtilityHelper) {
    //Next public holidays for the next 365 days
    private val publicHolidaysUrl = "https://date.nager.at/api/v3/NextPublicHolidays/CA"
    private val utilHelper = utilityHelper

    fun fetchData(tempFile: String){
        val url = URL(publicHolidaysUrl)
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty("Accept", "text/json")

        //Check connection status
        val responseCode = httpURLConnection.responseCode

        if(responseCode == HttpURLConnection.HTTP_OK){
            val dataStr = httpURLConnection.inputStream.bufferedReader().use {it.readText()}

            //Write data to temp file
            TempStorage(utilHelper).writeDataToFile(dataStr, tempFile)
        }else{
            Log.e("httpsURLConnection_ERROR", responseCode.toString())
        }
    }
}