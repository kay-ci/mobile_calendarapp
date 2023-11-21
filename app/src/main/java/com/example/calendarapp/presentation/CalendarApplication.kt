package com.example.calendarapp.presentation
import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CalendarApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Log.println(Log.DEBUG, "s","reached application")
    }
}