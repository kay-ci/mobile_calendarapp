package com.example.calendarapp.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Holiday(
    val date: String,
    val name: String
    )

fun getYear(dateString: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter)
    return date.year
}

fun getMonth(dateString: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter)
    return date.monthValue
}

fun getDay(dateString: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter)
    return date.dayOfMonth
}