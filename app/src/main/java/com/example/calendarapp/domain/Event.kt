package com.example.calendarapp.domain

import java.time.LocalDate
import java.time.LocalDateTime
data class Event(
    val title: String,
    val date: LocalDate,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val description: String,
    val location : String

    //we will use startTime as unique ID
)