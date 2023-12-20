package com.example.calendarapp

import android.app.Application
import android.icu.util.Calendar
import android.icu.util.ULocale
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.domain.Event
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import junit.framework.TestCase
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertTrue
import java.time.LocalDate
import java.time.LocalDateTime


@RunWith(AndroidJUnit4::class)
internal class CalendarViewModelTests {
    private lateinit var myViewModel: CalendarViewModel
    private var event1 = Event("Test", LocalDate.now(),
        LocalDateTime.now(),
        LocalDateTime.now().plusHours(2),"Description","Dawson","Teacher","Computer Science")

    @Before
    fun setUp() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        myViewModel = CalendarViewModel(application)
    }
    @Test
    fun getMonthNumber() {
        val expectedMonthNum1 = 1
        val expectedMonthNum2 = 12

        val actualMonth1 = myViewModel.getMonthNumber("January")
        val actualMonth2 = myViewModel.getMonthNumber("December")


        assertEquals(expectedMonthNum1, actualMonth1)
        assertEquals(expectedMonthNum2, actualMonth2)
    }

    @Test
    fun getMonthName() {
        val expectedMonth1 = "January"
        val expectedMonth2 = "December"

        val actualMonth1 = myViewModel.getMonthName(1)
        val actualMonth2 = myViewModel.getMonthName(2)

        assertEquals(expectedMonth1, actualMonth1)
        assertEquals(expectedMonth2, actualMonth2)
    }
    @Test
    fun nextMonth() {
        val currentMonth = myViewModel.currentMonth.value

        myViewModel.nextMonth()

        assertTrue(currentMonth != myViewModel.currentMonth.value)
    }
    @Test
    fun previousMonth() {
        val currentMonth = myViewModel.currentMonth.value

        myViewModel.previousMonth()

        assertTrue(currentMonth != myViewModel.currentMonth.value)
    }
    @Test
    fun getWeatherData() {
    }

    fun fetchWeatherData() {
    }

    fun fetchHolidayData() {
    }

    fun getHolidaysFromFile() {
    }

    fun getHolidaysForDate() {
    }
}