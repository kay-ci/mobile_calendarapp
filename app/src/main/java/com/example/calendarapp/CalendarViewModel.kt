package com.example.calendarapp

import android.icu.util.Calendar
import android.icu.util.ULocale
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class CalendarViewModel () : ViewModel() {
    private val _currentMonth = mutableStateOf("")
    val currentMonth: MutableState<String> = _currentMonth

    private val _currentYear = mutableStateOf(0)
    val currentYear: MutableState<Int> = _currentYear

    private val calendar: Calendar = Calendar.getInstance(ULocale("en_US@calendar=gregorian"))


    init {
        updateMonthYear()
    }

    fun getYear(): Int {
        return calendar.get(Calendar.YEAR)
    }
    private fun updateMonthYear() {
        val month = when (calendar.get(Calendar.MONTH)){
            0 -> "January"
            1 -> "February"
            2 -> "March"
            3 -> "April"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "August"
            8 -> "September"
            9 -> "October"
            10 -> "November"
            11 -> "December"
            else -> throw Exception("Invalid Month")
        }
        _currentMonth.value = month
        _currentYear.value = calendar.get(Calendar.YEAR)
    }
    fun nextMonth(){
        calendar.add(Calendar.MONTH, 1)
        updateMonthYear()
    }

    fun previousMonth(){
        calendar.add(Calendar.MONTH, -1)
        updateMonthYear()
    }


}