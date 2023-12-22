package com.example.calendarapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.data.EventDao
import com.example.calendarapp.data.EventRoomDatabase
import com.example.calendarapp.domain.Event
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime


//THE TESTS MUST BE RUN ONE AT A TIME BECAUSE SOME ARE RUN ON MAIN THREAD FOR OBSERVING THE LIVE DATA
@RunWith(AndroidJUnit4::class)
class EventDaoTest {
    private lateinit var eventDao: EventDao
    private lateinit var eventDatabase : EventRoomDatabase

    private var event1 = Event("Test", LocalDate.now(),LocalDateTime.now(),LocalDateTime.now().plusHours(2),"Description","Dawson","Teacher","Computer Science")
    private var event2 = Event("Test2", LocalDate.now().plusDays(2),LocalDateTime.now(),LocalDateTime.now().plusHours(2).plusDays(2),"School things","Library","Teacher","Computer Science")


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
    fun test_dao_insertIntoDB() = runBlocking {
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
    fun test_dao_getAllEvents() = runBlocking {
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

    @Test
    @Throws(Exception::class)
    fun test_dao_getEventByDate() = runBlocking {
        eventDao.insertEvent(event1)
        eventDao.insertEvent(event2)
        // Retrieve events based on the date
        val eventsOnDate = eventDao.findEvent(LocalDate.now())

        // Ensure the list is not empty
        assertTrue(eventsOnDate.isNotEmpty())

        // Get the first event from the list
        val firstEvent = eventsOnDate[0]

        assertEquals(event1.copy(id = firstEvent.id).title, firstEvent.title)

        // Assert that the second event is not in the list of events retrieved by findEvent
        assertFalse(eventsOnDate.contains(event2))
    }

    @Test
    @Throws(Exception::class)
    fun test_dao_updateEvents() = runBlocking {
        eventDao.insertEvent(event1)
        withContext(Dispatchers.Main) {
            eventDao.updateEvent(Event("Changed event", LocalDate.now(),LocalDateTime.now(),LocalDateTime.now().plusHours(2),"Description","Dawson","Teacher","Computer Science"))
            // Observe the LiveData
            val allEventsLiveData = eventDao.getAllEvents()
            allEventsLiveData.observeForever { allEvents ->
                // Get the first event from the list
                val firstEvent = allEvents[0]

                // Assert that the event in the db has been changed
                assertEquals(Event("Changed event", LocalDate.now(),LocalDateTime.now(),LocalDateTime.now().plusHours(2),"Description","Dawson","Teacher","Computer Science"), firstEvent)

                // Stop observing to avoid leaks
                allEventsLiveData.removeObserver {}
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun test_dao_deleteEvent() = runBlocking {
        eventDao.insertEvent(event1)
        eventDao.insertEvent(event2)
        withContext(Dispatchers.Main) {
            // Observe the LiveData
            val allEventsLiveData = eventDao.getAllEvents()
            allEventsLiveData.observeForever { allEvents ->
                // Get the first event from the list
                val firstEvent = allEvents[0]

                // Get the second event from the list
                val secondEvent = allEvents[1]

                firstEvent.id?.let { eventDao.deleteEvent(it) }
                secondEvent.id?.let { eventDao.deleteEvent(it) }

                assertTrue(allEvents.isEmpty())

                // Stop observing to avoid leaks
                allEventsLiveData.removeObserver {}
            }
        }
    }
}