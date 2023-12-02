package com.example.calendarapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.data.EventDao
import com.example.calendarapp.data.EventRoomDatabase
import com.example.calendarapp.domain.Event
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering
import java.io.IOException
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

    @After
    @Throws(IOException::class)
    fun closeDb() {
        eventDatabase.close()
    }
    @Test
    @Throws(Exception::class)
    fun testInsertIntoDB() = runBlocking {
        // Insert an event into the database
        eventDao.insertEvent(event1)

        // Switch to the main dispatcher to observe LiveData
        withContext(Dispatchers.Main) {
            // Observe the LiveData
            val allEventsLiveData = eventDao.getAllEvents()
            allEventsLiveData.observeForever { allEvents ->
                // Ensure the list is not empty
                assertTrue(allEvents.isNotEmpty())

                // Get the first event from the list
                val firstEvent = allEvents[0]

                // Assert that the first event matches the inserted event
                assertEquals(event1, firstEvent)

                // Stop observing to avoid leaks
                allEventsLiveData.removeObserver {}
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun testGetAllEventsDAO() = runBlocking {
        eventDao.insertEvent(event1)
        eventDao.insertEvent(event2)
        withContext(Dispatchers.Main) {
            // Observe the LiveData
            val allEventsLiveData = eventDao.getAllEvents()
            allEventsLiveData.observeForever { allEvents ->
                // Ensure the list is not empty
                assertTrue(allEvents.isNotEmpty())

                // Get the first event from the list
                val firstEvent = allEvents[0]
                // Get the second event from the list
                val secondEvent = allEvents[1]

                // Assert that the first and second event matches the inserted events
                assertEquals(event1, firstEvent)
                assertEquals(event2, secondEvent)

                // Stop observing to avoid leaks
                allEventsLiveData.removeObserver {}
            }
        }
    }


}