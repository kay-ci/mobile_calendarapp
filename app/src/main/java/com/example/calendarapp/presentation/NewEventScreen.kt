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
fun NewEventScreen(navController: NavHostController, viewModel: CalendarViewModel, option: String) {
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

    var displayMonth: String = month;
    if ("January" == month) {
        displayMonth = stringResource(R.string.january);
    } else if ("February" == month) {
        displayMonth = stringResource(R.string.february);
    } else if ("March" == month) {
        displayMonth = stringResource(R.string.march);
    } else if ("April" == month) {
        displayMonth = stringResource(R.string.april);
    } else if ("May" == month) {
        displayMonth = stringResource(R.string.may);
    } else if ("June" == month) {
        displayMonth = stringResource(R.string.june);
    } else if ("July" == month) {
        displayMonth = stringResource(R.string.july);
    } else if ("August" == month) {
        displayMonth = stringResource(R.string.august);
    } else if ("September" == month) {
        displayMonth = stringResource(R.string.september);
    } else if ("October" == month) {
        displayMonth = stringResource(R.string.october);
    } else if ("November" == month) {
        displayMonth = stringResource(R.string.november);
    } else if ("December" == month) {
        displayMonth = stringResource(R.string.december);
    }

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
                val startHourIsMandatory = stringResource(R.string.start_hour_is_mandatory)
                val startMinuteIsMandatory = stringResource(R.string.start_minute_is_mandatory)
                val endHourIsMandatory = stringResource(R.string.end_hour_is_mandatory)
                val endMinuteIsMandatory = stringResource(R.string.end_minute_is_mandatory)
                val dayIsMandatory = stringResource(R.string.day_is_mandatory)
                val eventCannotEndBeforeItStarts = stringResource(R.string.event_cannot_end_before_it_starts)
                val selectedTimeIsAlreadyUsedByAnotherEvent: String = stringResource(R.string.selected_time_is_already_used_by_another_event)
                val wrongTimeFormat = stringResource(R.string.time_information_not_in_expected_format_includes_day_field)
                Button(onClick = {
                    errorMessage = ""
                    if(title.isBlank())errorMessage += titleIsMandatory
                    if(startHour.isBlank())errorMessage += startHourIsMandatory
                    if(startMinute.isBlank())errorMessage += startMinuteIsMandatory
                    if(endHour.isBlank())errorMessage += endHourIsMandatory
                    if(endMinute.isBlank())errorMessage += endMinuteIsMandatory
                    if(day.isBlank() && option == "month")errorMessage += dayIsMandatory
                    try{
                        if(option == "day"){
                            day = LocalDate.parse(viewModel.selectedDate).dayOfMonth.toString()
                        }
                        var date = LocalDate.of(year, viewModel.getMonthNumber(month), day.toInt())
                        val start = LocalDateTime.of(date.year, date.month, date.dayOfMonth, startHour.toInt(), startMinute.toInt())
                        val end = LocalDateTime.of(date.year, date.month, date.dayOfMonth, endHour.toInt(), endMinute.toInt())
                        if(end <= start){
                            errorMessage += eventCannotEndBeforeItStarts
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
                                    errorMessage += selectedTimeIsAlreadyUsedByAnotherEvent + "(${loopEvent.title}). "
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
                        errorMessage += wrongTimeFormat
                    }
                }){
                    Text(stringResource(R.string.save))
                }
            }
        }
        item(){
            Text(errorMessage, color = Color.Red)
        }
        item(){
            Text("$displayMonth $year")
        }
        item(){
            Column(){
                Text(
                    text = stringResource(R.string.title) + ": ",
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
                    text = stringResource(R.string.description) + ": ",
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
                    text = stringResource(R.string.location) + ": ",
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
            if(option == "month"){
                Column(){
                    Text(
                        text = stringResource(R.string.day) + ": ",
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
            Column(){
                Text(
                    text = stringResource(R.string.teacher) + ": ",
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
                    text = stringResource(R.string.program) + ": ",
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
                    text = stringResource(R.string.start_hour) + ": ",
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
                    text = stringResource(R.string.start_minute) + ": ",
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
                    text = stringResource(R.string.end_hour) + ": ",
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
                    text = stringResource(R.string.end_minute) + ": ",
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

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun NewEventScreen2(navController: NavHostController, viewModel: CalendarViewModel, promptDay: Boolean) {
//    val allEvents by viewModel.allEvents.observeAsState()
//    var errorMessage by rememberSaveable {mutableStateOf("")}
//    var startMinute by rememberSaveable { mutableStateOf("") }
//    var startHour by rememberSaveable { mutableStateOf("") }
//    var endMinute by rememberSaveable { mutableStateOf("") }
//    var endHour by rememberSaveable { mutableStateOf("") }
//    var location by rememberSaveable { mutableStateOf("") }
//    var description by rememberSaveable { mutableStateOf("") }
//    var title by rememberSaveable { mutableStateOf("") }
//    var teacher by rememberSaveable {mutableStateOf("")}
//    var program by rememberSaveable {mutableStateOf("")}
//
//    val currentDate = LocalDate.parse(viewModel.selectedDate).format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
//    LazyColumn(){
//        item(){
//            NavigationBar(navController = navController)
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .background(Color.Gray)
//                    .fillMaxWidth()
//            ){
//                Text("New Event",
//                    modifier = Modifier.testTag("new_event_title"),
//                    fontSize = 40.sp,
//                    textAlign = TextAlign.Center,
//                    color = Color.White)
//                val titleIsMandatory = stringResource(R.string.title_is_mandatory)
//                val startHourIsMandatory = stringResource(R.string.start_hour_is_mandatory)
//                val startMinuteIsMandatory = stringResource(R.string.start_minute_is_mandatory)
//                val endHourIsMandatory = stringResource(R.string.end_hour_is_mandatory)
//                val endMinuteIsMandatory = stringResource(R.string.end_minute_is_mandatory)
//                val eventCannotEndBeforeItStarts = stringResource(R.string.event_cannot_end_before_it_starts)
//                val selectedTimeIsAlreadyUsedByAnotherEvent: String = stringResource(R.string.selected_time_is_already_used_by_another_event)
//                val wrongTimeFormat = stringResource(R.string.time_information_not_in_expected_format_includes_day_field)
//                Button(onClick = {
//                    errorMessage = ""
//                    if(title.isBlank())errorMessage += titleIsMandatory
//                    if(startHour.isBlank())errorMessage += startHourIsMandatory
//                    if(startMinute.isBlank())errorMessage += startMinuteIsMandatory
//                    if(endHour.isBlank())errorMessage += endHourIsMandatory
//                    if(endMinute.isBlank())errorMessage += endMinuteIsMandatory
//                    try{
//                        var date = LocalDate.parse(viewModel.selectedDate)
//                        val start = LocalDateTime.of(date.year, date.month, date.dayOfMonth, startHour.toInt(), startMinute.toInt())
//                        val end = LocalDateTime.of(date.year, date.month, date.dayOfMonth, endHour.toInt(), endMinute.toInt())
//                        if(end <= start){
//                            errorMessage += eventCannotEndBeforeItStarts
//                        }
//                        val newEvent = Event(title = title, date = date, startTime = start, endTime = end, description = description, location = location, teacher, program)
//                        var counter = 0
//                        allEvents?.forEach {loopEvent ->
//                            if(loopEvent.startTime.dayOfMonth == date.dayOfMonth){
//                                //make sure it starts and ends either before or after
//                                var startsBefore = start < loopEvent.startTime
//                                var endsBefore = end < loopEvent.startTime
//                                var startsAfter = start > loopEvent.endTime
//                                var endsAfter = end > loopEvent.endTime
//                                if(!(startsBefore && endsBefore || startsAfter && endsAfter) && counter == 0){
//                                    errorMessage += selectedTimeIsAlreadyUsedByAnotherEvent + " (${loopEvent.title}). "
//                                    counter++
//                                    //without the counter, the errorMessage can be appended many times.
//                                }
//                            }
//                        }
//                        if(errorMessage.isBlank()){
//                            viewModel.addEvent(newEvent)
//                            navController.popBackStack()
//                        }
//                    }
//                    catch(e: Exception) {
//                        errorMessage += wrongTimeFormat
//                    }
//                }, modifier = Modifier.padding(5.dp).testTag("save_event")){
//                    Text(stringResource(R.string.save))
//                }
//            }
//        }
//        item(){
//            Text(errorMessage, color = Color.Red)
//            Text(currentDate)
//        }
//        item(){
//            Column(){
//                Text(
//                    text = stringResource(R.string.title)+": ",
//                    fontSize = 40.sp
//                )
//                TextField(
//                    value = title,
//                    onValueChange = { title = it},
//                    maxLines = 1,
//                    modifier = Modifier.fillMaxWidth()
//                        .testTag("title")
//                )
//            }
//            Column(){
//                Text(
//                    text = stringResource(R.string.description)+": ",
//                    fontSize = 40.sp
//                )
//                TextField(
//                    value = description,
//                    onValueChange = { description = it},
//                    maxLines = 1,
//                    modifier = Modifier.fillMaxWidth()
//                        .testTag("description")
//                )
//            }
//            Column(){
//                Text(
//                    text = stringResource(R.string.location)+": ",
//                    fontSize = 40.sp
//                )
//                TextField(
//                    value = location,
//                    onValueChange = { location = it},
//                    maxLines = 1,
//                    modifier = Modifier.fillMaxWidth()
//                        .testTag("location")
//                )
//            }
//
//            Column(){
//                Text(
//                    text = stringResource(R.string.teacher)+": ",
//                    fontSize = 40.sp
//                )
//                TextField(
//                    value = teacher,
//                    onValueChange = { teacher = it},
//                    maxLines = 1,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//            Column(){
//                Text(
//                    text = stringResource(R.string.program)+": ",
//                    fontSize = 40.sp
//                )
//                TextField(
//                    value = program,
//                    onValueChange = { program = it},
//                    maxLines = 1,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//
//        }
//        item(){
//            Column(){
//                Text(
//                    text = stringResource(R.string.start_hour)+": ",
//                    fontSize = 40.sp
//                )
//                TextField(
//                    value = startHour,
//                    onValueChange = { startHour = it},
//                    maxLines = 1,
//                    modifier = Modifier.fillMaxWidth()
//                        .testTag("start_hour")
//                )
//            }
//            Column(){
//                Text(
//                    text = stringResource(R.string.start_minute)+": ",
//                    fontSize = 40.sp
//                )
//                TextField(
//                    value = startMinute,
//                    onValueChange = { startMinute = it},
//                    maxLines = 1,
//                    modifier = Modifier.fillMaxWidth()
//                        .testTag("start_minute")
//                )
//            }
//        }
//        item(){
//            Column(){
//                Text(
//                    text = stringResource(R.string.end_hour)+": ",
//                    fontSize = 40.sp
//                )
//                TextField(
//                    value = endHour,
//                    onValueChange = { endHour = it},
//                    maxLines = 1,
//                    modifier = Modifier.fillMaxWidth()
//                        .testTag("end_hour")
//                )
//            }
//            Column(){
//                Text(
//                    text = stringResource(R.string.end_minute)+": ",
//                    fontSize = 40.sp
//                )
//                TextField(
//                    value = endMinute,
//                    onValueChange = { endMinute = it},
//                    maxLines = 1,
//                    modifier = Modifier.fillMaxWidth()
//                        .testTag("end_minute")
//                )
//            }
//        }
//    }
//}