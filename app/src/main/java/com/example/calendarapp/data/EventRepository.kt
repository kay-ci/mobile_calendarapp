package com.example.calendarapp.data

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import androidx.lifecycle.LiveData
import com.example.calendarapp.domain.Event
import java.time.LocalDate

class EventRepository(private val eventDao: EventDao) {
    private val searchResults = MutableLiveData<List<Event>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertEvent(newEvent: Event){
        coroutineScope.launch(Dispatchers.IO){
            eventDao.insertEvent(newEvent)
        }
    }
    fun deleteEvent(title: String) {
        coroutineScope.launch(Dispatchers.IO) {
            eventDao.deleteEvent(title)
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