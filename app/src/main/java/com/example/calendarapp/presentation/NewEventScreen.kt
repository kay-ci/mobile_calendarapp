package com.example.calendarapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.calendarapp.R
import com.example.calendarapp.domain.Event
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NewMonthEventScreen(navController: NavHostController, month: String?) {
    LazyColumn(){
        item(){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.background(Color.Gray)
                    .fillMaxWidth()
            ){
                Button(onClick = {
                    navController.popBackStack()
                }){
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = "back arrow"
                    )
                }
                Text("New Event",
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White)
                Button(onClick = {
                    navController.popBackStack()
                }){
                    Text("save")
                }
            }
        }//use TimeDialog
        item(){
            Field(true, "Title", "")
        }
        item(){
            Field(true, "description", "")
        }
        item(){
            Text("Start Time:", fontSize = 40.sp)
            Field(true, "Day", "")
            Field(false, "Month", month)
            Field(true, "Year", "")
            Field(true, "Hour", "")
            Field(true, "Minute", "")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDayEventScreen(navController: NavHostController, date: LocalDate, viewModel: CalendarViewModel) {
    var startMinute by rememberSaveable { mutableStateOf("") }
    var startHour by rememberSaveable { mutableStateOf("") }
    var endMinute by rememberSaveable { mutableStateOf("") }
    var endHour by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }

    val currentDate = date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
    val day = date.format(DateTimeFormatter.ofPattern("d"))
    val month = date.format(DateTimeFormatter.ofPattern("MMMM"))
    LazyColumn(){
        item(){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.background(Color.Gray)
                    .fillMaxWidth()
            ){
                Button(onClick = {
                    navController.popBackStack()
                }){
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = "back arrow"
                    )
                }
                Text("New Event",
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White)
                Button(onClick = {
                    try{
                        val start = LocalDateTime.of(2023, 11, 20, startHour.toInt(), startMinute.toInt())
                        val end = LocalDateTime.of(2023, 11, 20, endHour.toInt(), endHour.toInt())
                        val date = LocalDate.of(2023, 11, 20)
                        val newEvent = Event(title = title, date = date, startTime = start, endTime = end, description = description, location = location)
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