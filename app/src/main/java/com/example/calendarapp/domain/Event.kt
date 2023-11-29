package com.example.calendarapp.domain
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "eventId") var id: Int?,
    @ColumnInfo(name ="eventTitle") @NonNull var title: String,
    @ColumnInfo(name ="eventDate") @NonNull val date: LocalDate,
    @ColumnInfo(name ="eventStart") @NonNull val startTime: LocalDateTime,
    @ColumnInfo(name ="eventEnd") @NonNull var endTime: LocalDateTime,
    @ColumnInfo(name ="eventDesc")var description: String?,
    @ColumnInfo(name ="eventLoc")var location : String?,
    @ColumnInfo(name ="eventTeacher")var teacher: String?,
    @ColumnInfo(name ="eventProgram")var program: String?

){
    constructor(
        title: String,
        date: LocalDate,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        description: String?,
        location: String?,
        teacher: String?,
        program: String?
    ) : this(null, title, date, startTime, endTime, description, location, teacher, program)
}