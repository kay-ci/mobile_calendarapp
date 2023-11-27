package com.example.calendarapp.domain
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "eventId") var id: Int = 0,
    @ColumnInfo(name ="eventTitle")var title: String,
    @ColumnInfo(name ="eventDate")val date: LocalDate,
    @ColumnInfo(name ="eventStart")val startTime: LocalDateTime,
    @ColumnInfo(name ="eventEnd")var endTime: LocalDateTime,
    @ColumnInfo(name ="eventDesc")var description: String?,
    @ColumnInfo(name ="eventLoc")var location : String?

    //we will use startTime as unique ID
)