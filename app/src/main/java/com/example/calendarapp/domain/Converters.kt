package com.example.calendarapp.domain

import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.annotation.Nullable

class Converters {
    @TypeConverter
    fun fromTimestampForDay(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC) }
    }
    @TypeConverter
    fun dateToTimestampForDay(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }
    @RequiresApi(34)
    @TypeConverter
    fun fromTimestampForDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestampForDate(date: LocalDate?): Long? {
        return date?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }

}