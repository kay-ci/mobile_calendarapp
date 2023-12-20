package com.example.calendarapp.data

import android.content.Context
import android.util.Log
import java.io.IOException

class TempStorage(context: Context) {
    private val appContext = context

    fun writeDataToFile(dataString: String, filename: String){
        try {
            appContext.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(dataString.toByteArray())
            }
        }catch (e: IOException){
            Log.d("fetchData", "Error writing data to file: ${e.message}")
        }
    }

    fun readDataFromFile(filename: String): String{
        //String to hold data
        var data = ""
        try {
            data = appContext.openFileInput(filename).bufferedReader().use {
                it.readText()
            }
        } catch (e: IOException){
            Log.e("readDataFromFile", "ERROR reading data from file")
        }
        return data
    }
}