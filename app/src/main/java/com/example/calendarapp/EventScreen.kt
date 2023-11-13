package com.example.calendarapp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun EventScreen(event: Event){
    Column() {
        Text("title: " + event.title)
        Text("start: " + event.startTime)
        Text("end: " + event.endTime)
        Text("description: " + event.description)
        
    }
}