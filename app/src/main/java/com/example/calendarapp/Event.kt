package com.example.calendarapp

import java.time.LocalDateTime
data class Event(
    val title: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val description: String
)
