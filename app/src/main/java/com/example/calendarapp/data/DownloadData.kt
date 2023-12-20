package com.example.calendarapp.data

import android.content.Context
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

class DownloadData (context: Context) {
    private val appContext = context

    fun fetchData(tempFile: String, url: String){
        val url = URL(url)
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty("Accept", "text/json")

        //Check connection status
        val responseCode = httpURLConnection.responseCode

        if(responseCode == HttpURLConnection.HTTP_OK){
            val dataStr = httpURLConnection.inputStream.bufferedReader().use {it.readText()}

            //Write data to temp file
            TempStorage(appContext).writeDataToFile(dataStr, tempFile)
        }else{
            Log.e("httpsURLConnection_ERROR", responseCode.toString())
        }
    }
}