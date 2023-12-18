package com.example.calendarapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.calendarapp.R
import com.example.calendarapp.domain.Event
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMonthEventScreen(navController: NavHostController, viewModel: CalendarViewModel) {
    val allEvents by viewModel.allEvents.observeAsState()
    var errorMessage by rememberSaveable {mutableStateOf("")}
    var startMinute by rememberSaveable { mutableStateOf("") }
    var startHour by rememberSaveable { mutableStateOf("") }
    var endMinute by rememberSaveable { mutableStateOf("") }
    var endHour by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }
    var teacher by rememberSaveable {mutableStateOf("")}
    var program by rememberSaveable {mutableStateOf("")}

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
                Text(
                                    stringResource(R.string.new_event),
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White)
                val titleIsMandatory = stringResource(R.string.title_is_mandatory)
                val endHourIsMandatory = stringResource(R.string.title_is_mandatory)
                val endMinuteIsMandatory = stringResource(R.string.title_is_mandatory)
                val eventCannotEndBeforeItStarts = stringResource(R.string.event_cannot_end_before_it_starts)
                val selectedTimeIsAlreadyUsedByAnotherEvent: String = stringResource(R.string.selected_time_is_already_used_by_another_event)
                Button(onClick = {
                    errorMessage = ""
                    if(title.isBlank())errorMessage += titleIsMandatory
                    if(startHour.isBlank())errorMessage += "Start hour is mandatory. "
                    if(startMinute.isBlank())errorMessage += "Start Minute is mandatory. "
                    if(endHour.isBlank())errorMessage += "End hour is mandatory. "
                    if(endMinute.isBlank())errorMessage += "End minute is mandatory. "
                    if(day.isBlank())errorMessage += "Day is mandatory. "
                    try{
                        val date = LocalDate.of(year, viewModel.getMonthNumber(month), day.toInt())
                        val start = LocalDateTime.of(date.year, date.month, date.dayOfMonth, startHour.toInt(), startMinute.toInt())
                        val end = LocalDateTime.of(date.year, date.month, date.dayOfMonth, endHour.toInt(), endMinute.toInt())
                        if(end <= start){
                            errorMessage += "Event cannot end before it starts. "
                        }
                        val newEvent = Event(title = title, date = date, startTime = start, endTime = end, description = description, location = location, teacher, program)
                        var counter = 0
                        allEvents?.forEach {loopEvent ->
                            if(loopEvent.startTime.dayOfMonth.toString() == day){
                                //make sure it starts and ends either before or after
                                var startsBefore = start < loopEvent.startTime
                                var endsBefore = end < loopEvent.startTime
                                var startsAfter = start > loopEvent.endTime
                                var endsAfter = end > loopEvent.endTime
                                if(!(startsBefore && endsBefore || startsAfter || endsAfter) && counter == 0){
                                    errorMessage += "Selected time is already used by another event (${loopEvent.title}). "
                                    counter++
                                    //without the counter, the errorMessage can be appended many times.
                                }
                            }
                        }
                        if(errorMessage.isBlank()){
                            viewModel.addEvent(newEvent)
                            navController.popBackStack()
                        }
                    }
                    catch(e: Exception){
                        errorMessage += "Time information not in expected format (includes day field). "
                    }
                }){
                    Text("save")
                }
            }
        }
        item(){
            Text(errorMessage, color = Color.Red)
        }
        item(){
            Text("$month $year")
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
                        .testTag("title"),
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
                        .testTag("description"),
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
                        .testTag("location"),
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
            Column(){
                Text(
                    text = "Teacher: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = teacher,
                    onValueChange = { teacher = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(){
                Text(
                    text = "Program: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = program,
                    onValueChange = { program = it},
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
                        .testTag("start_hour"),
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
                        .testTag("start_minute"),
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
                        .testTag("end_hour"),
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
                        .testTag("end_minute"),
                )
            }
        }
        item(){
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDayEventScreen(navController: NavHostController, viewModel: CalendarViewModel) {
    val allEvents by viewModel.allEvents.observeAsState()
    var errorMessage by rememberSaveable {mutableStateOf("")}
    var startMinute by rememberSaveable { mutableStateOf("") }
    var startHour by rememberSaveable { mutableStateOf("") }
    var endMinute by rememberSaveable { mutableStateOf("") }
    var endHour by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }
    var teacher by rememberSaveable {mutableStateOf("")}
    var program by rememberSaveable {mutableStateOf("")}

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
                    modifier = Modifier.testTag("new_event_title"),
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White)
                Button(onClick = {
                    errorMessage = ""
                    if(title.isBlank())errorMessage += "Title is mandatory. "
                    if(startHour.isBlank())errorMessage += "Start hour is mandatory. "
                    if(startMinute.isBlank())errorMessage += "Start Minute is mandatory. "
                    if(endHour.isBlank())errorMessage += "End hour is mandatory. "
                    if(endMinute.isBlank())errorMessage += "End minute is mandatory. "
                    try{
                        var date = LocalDate.parse(viewModel.selectedDate)
                        val start = LocalDateTime.of(date.year, date.month, date.dayOfMonth, startHour.toInt(), startMinute.toInt())
                        val end = LocalDateTime.of(date.year, date.month, date.dayOfMonth, endHour.toInt(), endMinute.toInt())
                        if(end <= start){
                            errorMessage += "Event cannot end before it starts. "
                        }
                        val newEvent = Event(title = title, date = date, startTime = start, endTime = end, description = description, location = location, teacher, program)
                        var counter = 0
                        allEvents?.forEach {loopEvent ->
                            if(loopEvent.startTime.dayOfMonth == date.dayOfMonth){
                                //make sure it starts and ends either before or after
                                var startsBefore = start < loopEvent.startTime
                                var endsBefore = end < loopEvent.startTime
                                var startsAfter = start > loopEvent.endTime
                                var endsAfter = end > loopEvent.endTime
                                if(!(startsBefore && endsBefore || startsAfter && endsAfter) && counter == 0){
                                    errorMessage += "Selected time is already used by another event (${loopEvent.title}). "
                                    counter++
                                    //without the counter, the errorMessage can be appended many times.
                                }
                            }
                        }
                        if(errorMessage.isBlank()){
                            viewModel.addEvent(newEvent)
                            navController.popBackStack()
                        }
                    }
                    catch(e: Exception) {
                        errorMessage += "Time information not in expected format (includes day field). "
                    }
                }, modifier = Modifier.padding(5.dp).testTag("save_event")){
                    Text("save")
                }
            }
        }
        item(){
            Text(errorMessage, color = Color.Red)
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
                        .testTag("title")
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
                        .testTag("description")
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
                        .testTag("location")
                )
            }

            Column(){
                Text(
                    text = "teacher: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = teacher,
                    onValueChange = { teacher = it},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(){
                Text(
                    text = "program: ",
                    fontSize = 40.sp
                )
                TextField(
                    value = program,
                    onValueChange = { program = it},
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
                        .testTag("start_hour")
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
                        .testTag("start_minute")
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
                        .testTag("end_hour")
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
                        .testTag("end_minute")
                )
            }
        }
    }
}