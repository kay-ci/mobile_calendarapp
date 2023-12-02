package com.example.calendarapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.data.EventDao
import com.example.calendarapp.data.EventRoomDatabase
import com.example.calendarapp.domain.Event
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@RunWith(AndroidJUnit4::class)
class EventDaoTest {
    private lateinit var eventDao: EventDao
    private lateinit var eventDatabase : EventRoomDatabase

    private var event1 = Event("Test", LocalDate.now(),LocalDateTime.now(),LocalDateTime.now().plusHours(2),"Description","Dawson")
    private var event2 = Event("Test2", LocalDate.now().plusDays(2),LocalDateTime.now(),LocalDateTime.now().plusHours(2).plusDays(2),"School things","Library")

    @Before
    fun createDB() {
        val context: Context = ApplicationProvider.getApplicationContext()

        eventDatabase = Room.inMemoryDatabaseBuilder(context, EventRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        eventDao = eventDatabase.eventDao()
    }
}