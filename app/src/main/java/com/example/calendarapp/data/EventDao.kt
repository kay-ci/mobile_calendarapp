package com.example.calendarapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.calendarapp.domain.Event
import java.time.LocalDate


interface EventDao {
    @Insert
    fun insertEvent(event : Event)

    @Query("SELECT * FROM events WHERE eventDate = :date")
    fun findEvent(date: LocalDate) : List<Event>
}