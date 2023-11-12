package com.example.calendarapp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import android.icu.util.Calendar
import android.icu.util.ULocale
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

class CalendarViewModel () : ViewModel() {
    private val calendar: Calendar = Calendar.getInstance(ULocale("en_US@calendar=gregorian"))
    private val _currentMonth = mutableStateOf(calendar.time)

    init {
        calendar.time = Date();
    }

    fun nextMonth(){
        calendar.add(Calendar.MONTH, 1)
    }

    fun previousMonth(){
        calendar.add(Calendar.MONTH, -1)

    }
}