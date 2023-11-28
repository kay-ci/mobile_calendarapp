package com.example.calendarapp.data

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import androidx.lifecycle.LiveData
import com.example.calendarapp.domain.Event
import java.time.LocalDate

class EventRepository(private val eventDao: EventDao) {
    val searchResults = MutableLiveData<List<Event>>()
    val allEvents : LiveData<List<Event>> = eventDao.getAllEvents()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertEvent(newEvent: Event){
        coroutineScope.launch(Dispatchers.IO){
            eventDao.insertEvent(newEvent)
        }
    }
    fun deleteEvent(eventId: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            eventDao.deleteEvent(eventId)
        }
    }
    fun updateEvent(updatedEvent: Event) {
        coroutineScope.launch(Dispatchers.IO) {
            eventDao.updateEvent(updatedEvent)
        }
    }

    fun findEvent(date : LocalDate){
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(date).await()
        }
    }
    private fun asyncFind(date : LocalDate): Deferred<List<Event>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async eventDao.findEvent(date)
        }




}