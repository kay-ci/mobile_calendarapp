package com.example.calendarapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.calendarapp.domain.Event
import java.time.LocalDate


@Dao
interface EventDao {
    @Insert
    fun insertEvent(event : Event)

    @Query("SELECT * FROM events WHERE eventDate = :date")
    fun findEvent(date: LocalDate) : List<Event>

    @Query("DELETE FROM events WHERE eventTitle= :title")
    fun deleteEvent(title : String)

    @Query("SELECT * from events")
    fun getAllEvents(): LiveData<List<Event>>


}