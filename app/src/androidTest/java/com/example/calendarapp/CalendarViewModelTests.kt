package com.example.calendarapp

import android.app.Application
import android.icu.util.Calendar
import android.icu.util.ULocale
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.data.TempStorage
import com.example.calendarapp.domain.Event
import com.example.calendarapp.domain.Holiday
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import junit.framework.TestCase
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import java.time.LocalDate
import java.time.LocalDateTime


@RunWith(AndroidJUnit4::class)
internal class CalendarViewModelTests {
    private lateinit var myViewModel: CalendarViewModel
    private lateinit var application: Application
    private var event1 = Event("Test", LocalDate.now(),
        LocalDateTime.now(),
        LocalDateTime.now().plusHours(2),"Description","Dawson","Teacher","Computer Science")

    @Before
    fun setUp() {
        application = ApplicationProvider.getApplicationContext<Application>()
        myViewModel = CalendarViewModel(application)
    }
    @Test
    fun getMonthNumber() {
        // Arrange
        val expectedMonthNum1 = 1
        val expectedMonthNum2 = 12

        // Act
        val actualMonth1 = myViewModel.getMonthNumber("January")
        val actualMonth2 = myViewModel.getMonthNumber("December")

        // Assert
        assertEquals(expectedMonthNum1, actualMonth1)
        assertEquals(expectedMonthNum2, actualMonth2)
    }

    @Test
    fun getMonthName() {
        // Arrange
        val expectedMonth1 = "January"
        val expectedMonth2 = "December"

        // Act
        val actualMonth1 = myViewModel.getMonthName(1)
        val actualMonth2 = myViewModel.getMonthName(12)

        // Assert
        assertEquals(expectedMonth1, actualMonth1)
        assertEquals(expectedMonth2, actualMonth2)
    }
    @Test
    fun nextMonth() {
        // Arrange
        val currentMonth = myViewModel.currentMonth.value

        // Act
        myViewModel.nextMonth()

        // Assert
        assertTrue(currentMonth != myViewModel.currentMonth.value)
    }
    @Test
    fun previousMonth() {
        // Arrange
        val currentMonth = myViewModel.currentMonth.value

        // Act
        myViewModel.previousMonth()

        // Assert
        assertTrue(currentMonth != myViewModel.currentMonth.value)
    }

    @Test
    fun getDayOfWeek(){
        // Arrange
        val dateString = "2023-12-19 12:00:00"

        // Act
        val dayOfWeek = myViewModel.getDayOfWeek(dateString)

        // Assert
        assertEquals("Tuesday", dayOfWeek)
    }

    @Test
    fun fetchHolidayData(){
        // Arrange
        val emptyData = myViewModel.holidayData.value
        assertEquals("", emptyData)

        // Act
        myViewModel.fetchHolidayData()
        myViewModel.getHolidaysFromFile()

        val fileData = TempStorage(application).readDataFromFile("holidayData")

        // Assert
        assertTrue("" != fileData)
    }
}