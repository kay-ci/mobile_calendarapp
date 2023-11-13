package com.example.calendarapp

import android.icu.util.Calendar
import android.icu.util.ULocale
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class CalendarViewModel () : ViewModel() {
    private val calendar: Calendar = Calendar.getInstance(ULocale("en_US@calendar=gregorian"))

    private val _currentMonth = mutableStateOf("")
    val currentMonth: MutableState<String> = _currentMonth

    private val _currentYear = mutableStateOf(0)
    val currentYear: MutableState<Int> = _currentYear

    private val _daysInMonth = mutableStateOf<List<String>>(emptyList())
    val daysInMonth : MutableState<List<String>> = _daysInMonth

    private val _firstWeekDay = mutableStateOf<Int>(Calendar.SUNDAY)
    val firstWeekDay : MutableState<Int> = _firstWeekDay

    init {
        updateMonthYear()
        updateDaysOfMonth()
        updateFirstWeekDay()
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
        updateDaysOfMonth()
    }

    fun previousMonth(){
        calendar.add(Calendar.MONTH, -1)
        updateMonthYear()
        updateDaysOfMonth()
    }
    private fun updateDaysOfMonth(){
        val days = mutableListOf<String>()
        val totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (day in 1..totalDays) {
            days.add(day.toString())
        }

        _daysInMonth.value = days
    }

    private fun updateFirstWeekDay(){
        firstWeekDay.value = calendar.get(Calendar.DAY_OF_WEEK)
    }

}