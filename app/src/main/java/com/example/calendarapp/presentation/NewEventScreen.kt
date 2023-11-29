package com.example.calendarapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.calendarapp.domain.Event
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMonthEventScreen(navController: NavHostController, viewModel: CalendarViewModel) {
    var startMinute by rememberSaveable { mutableStateOf("") }
    var startHour by rememberSaveable { mutableStateOf("") }
    var endMinute by rememberSaveable { mutableStateOf("") }
    var endHour by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }

    var day by rememberSaveable {mutableStateOf("")}

    val year = viewModel.currentYear.value
    val month = viewModel.currentMonth.value

    LazyColumn(){
        item(){
            NavigationBar(navController = navController)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
            ){
                Text("New Event",
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White)
                Button(onClick = {
                    try{
                        val date = LocalDate.of(year, viewModel.getMonthNumber(month), day.toInt())
                        val start = LocalDateTime.of(date.year, date.month, date.dayOfMonth, startHour.toInt(), startMinute.toInt())
                        val end = LocalDateTime.of(date.year, date.month, date.dayOfMonth, endHour.toInt(), endMinute.toInt())
                        if(end <= start){
                            throw Exception("chronologically impossible")
                        }
                        val newEvent = Event(title = title, date = date, startTime = start, endTime = end, description = description, location = location)
                        if(viewModel.containsEvent(newEvent)){
                            throw Exception("time is already used by another event")
                        }
                        viewModel.addEvent(newEvent)
                        navController.popBackStack()
                    }
                    catch(e: Exception){
                        //show error message
                    }
                }){
                    Text("save")
                }
            }
        }
        item(){
            Text(month + " "+ year)
        }
        item(){
            Column(){
                Text(
                    text = "Title: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = title,
                    onValueChange = { title = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(){
                Text(
                    text = "description: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = description,
                    onValueChange = { description = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(){
                Text(
                    text = "location: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = location,
                    onValueChange = { location = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(){
                Text(
                    text = "day: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = day,
                    onValueChange = { day = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item(){
            Column(){
                Text(
                    text = "start hour: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = startHour,
                    onValueChange = { startHour = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(){
                Text(
                    text = "start minute: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = startMinute,
                    onValueChange = { startMinute = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item(){
            Column(){
                Text(
                    text = "end hour: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = endHour,
                    onValueChange = { endHour = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(){
                Text(
                    text = "end minute: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = endMinute,
                    onValueChange = { endMinute = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDayEventScreen(navController: NavHostController, viewModel: CalendarViewModel) {
    var startMinute by rememberSaveable { mutableStateOf("") }
    var startHour by rememberSaveable { mutableStateOf("") }
    var endMinute by rememberSaveable { mutableStateOf("") }
    var endHour by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }

    val currentDate = LocalDate.parse(viewModel.selectedDate).format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
    LazyColumn(){
        item(){
            NavigationBar(navController = navController)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
            ){
                Text("New Event",
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White)
                Button(onClick = {
                    try{
                        val date = LocalDate.parse(viewModel.selectedDate)
                        val start = LocalDateTime.of(date.year, date.month, date.dayOfMonth, startHour.toInt(), startMinute.toInt())
                        val end = LocalDateTime.of(date.year, date.month, date.dayOfMonth, endHour.toInt(), endMinute.toInt())
                        if(end <= start){
                            throw Exception()
                        }
                        val newEvent = Event(title = title, date = date, startTime = start, endTime = end, description = description, location = location)
                        if(viewModel.containsEvent(newEvent)){
                            throw Exception()
                        }
                        viewModel.addEvent(newEvent)
                        navController.popBackStack()
                    }
                    catch(e: Exception){
                        //show error message
                    }
                }){
                    Text("save")
                }
            }
        }
        item(){
            Text(currentDate)
        }
        item(){
            Column(){
                Text(
                    text = "Title: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = title,
                    onValueChange = { title = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(){
                Text(
                    text = "description: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = description,
                    onValueChange = { description = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(){
                Text(
                    text = "location: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = location,
                    onValueChange = { location = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item(){
            Column(){
                Text(
                    text = "start hour: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = startHour,
                    onValueChange = { startHour = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(){
                Text(
                    text = "start minute: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = startMinute,
                    onValueChange = { startMinute = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item(){
            Column(){
                Text(
                    text = "end hour: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = endHour,
                    onValueChange = { endHour = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(){
                Text(
                    text = "end minute: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = endMinute,
                    onValueChange = { endMinute = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}