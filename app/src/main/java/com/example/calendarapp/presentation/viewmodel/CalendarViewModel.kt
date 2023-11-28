package com.example.calendarapp.presentation.viewmodel

import android.app.Application
import android.icu.util.Calendar
import android.icu.util.ULocale
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calendarapp.data.EventRepository
import com.example.calendarapp.data.EventRoomDatabase
import com.example.calendarapp.domain.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate

class CalendarViewModel (application: Application) : ViewModel() {
    var selectedDate by mutableStateOf("")
    fun setDate(newDate: String){
        selectedDate = newDate
    }

    private val calendar: Calendar = Calendar.getInstance(ULocale("en_US@calendar=gregorian"))

    private val _currentDay = mutableStateOf(0)
    val currentDay: MutableState<Int> = _currentDay

    private val _currentMonth = mutableStateOf("")
    val currentMonth: MutableState<String> = _currentMonth

    private val _currentYear = mutableStateOf(0)
    val currentYear: MutableState<Int> = _currentYear

    private val _daysInMonth = mutableStateOf<List<String>>(emptyList())
    val daysInMonth : MutableState<List<String>> = _daysInMonth

    private val _firstWeekDay = mutableStateOf<Int>(Calendar.SUNDAY)
    val firstWeekDay : MutableState<Int> = _firstWeekDay
    val allEvents : LiveData<List<Event>>
    private val repository : EventRepository
    val searchResults: MutableLiveData<List<Event>>

    init {
        updateMonthYear()
        updateDaysOfMonth()
        updateFirstWeekDay()
        val eventDb = EventRoomDatabase.getInstance(application)
        val eventDao = eventDb.eventDao()
        repository = EventRepository(eventDao)

        allEvents = repository.allEvents
        searchResults = repository.searchResults


    }

    fun getMonthNumber(month: String): Int {
        return when (month) {
            "January" -> 1
            "February" -> 2
            "March" -> 3
            "April" -> 4
            "May" -> 5
            "June" -> 6
            "July" -> 7
            "August" -> 8
            "September" -> 9
            "October" -> 10
            "November" -> 11
            "December" -> 12
            else -> -1 // Return -1 for invalid input
        }
    }

    fun getMonthName(monthNumber: Int): String {
        return when (monthNumber) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Invalid month number"
        }
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
        updateFirstWeekDay()
    }

    fun previousMonth(){
        calendar.add(Calendar.MONTH, -1)
        updateMonthYear()
        updateDaysOfMonth()
        updateFirstWeekDay()
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

    private val _events = mutableStateListOf<Event>()
    val events: List<Event> = _events
    fun getEventsForDate(date: LocalDate){
        repository.findEvent(date)
    }
    fun addEvent(event: Event){
        repository.insertEvent(event)
    }

    fun removeEvent(index: Int){
        repository.deleteEvent(index)
    }

    fun containsEvent(theEvent: Event): Boolean{
        var output: Boolean = false
        _events.forEach{event ->
            if(event.startTime == theEvent.startTime){
                output = true
            }
        }

        return output
    }

}