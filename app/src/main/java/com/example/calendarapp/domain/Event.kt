package com.example.calendarapp.domain

import java.time.LocalDate
import java.time.LocalDateTime
data class Event(
    var title: String,
    val date: LocalDate,
    val startTime: LocalDateTime,
    var endTime: LocalDateTime,
    var description: String,
    var location : String

    //we will use startTime as unique ID
)