package com.example.calendarapp.presentation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.calendarapp.domain.Event
import com.example.calendarapp.R
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(event: Event, navController: NavHostController, viewModel: CalendarViewModel){
    val allEvents by viewModel.allEvents.observeAsState()
    var errorMessage by rememberSaveable {mutableStateOf("")}
    var editable: Boolean by rememberSaveable{mutableStateOf(false)}
    var title by rememberSaveable { mutableStateOf(event.title) }
    var description by rememberSaveable { mutableStateOf(event.description) }
    var location by rememberSaveable { mutableStateOf(event.location) }
    var day by rememberSaveable { mutableStateOf(event.date.dayOfMonth.toString()) }
    var month by rememberSaveable { mutableStateOf(event.date.monthValue.toString()) }
    var year by rememberSaveable { mutableStateOf(event.date.year.toString()) }
    var startHour by rememberSaveable { mutableStateOf(event.startTime.hour.toString()) }
    var startMinute by rememberSaveable { mutableStateOf(event.startTime.minute.toString()) }
    var endHour by rememberSaveable { mutableStateOf(event.endTime.hour.toString()) }
    var endMinute by rememberSaveable { mutableStateOf(event.endTime.minute.toString()) }
    var teacher by rememberSaveable {mutableStateOf(event.teacher)}
    var program by rememberSaveable {mutableStateOf(event.program)}

    //Allow the changes in the event view to be seen directly when you edit an event
    DisposableEffect(event) {
        // Set the state variables based on the event parameter
        title = event.title
        description = event.description
        location = event.location
        day = event.date.dayOfMonth.toString()
        month = event.date.monthValue.toString()
        year = event.date.year.toString()
        startHour = event.startTime.hour.toString()
        startMinute = event.startTime.minute.toString()
        endHour = event.endTime.hour.toString()
        endMinute = event.endTime.minute.toString()
        teacher = event.teacher
        program = event.program
        onDispose {
            // Cleanup code if needed
        }
    }
    LazyColumn{
        item{
            NavigationBar(navController = navController)
            Row(){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .background(Color.Gray)
            ){
                Button(onClick = {
                    navController.popBackStack()
                }){
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = stringResource(R.string.back_arrow)
                    )
                }
                Button(onClick = {editable = !editable},
                    colors = if (editable) ButtonDefaults.buttonColors(Color.DarkGray) else ButtonDefaults.buttonColors())
                {
                    Text(stringResource(R.string.edit))
                }
                val titleIsMandatory = stringResource(R.string.title_is_mandatory)
                val endHourIsMandatory = stringResource(R.string.title_is_mandatory)
                val endMinuteIsMandatory = stringResource(R.string.title_is_mandatory)
                val eventCannotEndBeforeItStarts = stringResource(R.string.event_cannot_end_before_it_starts)
                val selectedTimeIsAlreadyUsedByAnotherEvent: String = stringResource(R.string.selected_time_is_already_used_by_another_event)
                Button(onClick = {
                    errorMessage = ""
                    if(title.isBlank())errorMessage += titleIsMandatory
                    if(endHour.isBlank())errorMessage += endHourIsMandatory
                    if(endMinute.isBlank())errorMessage += endMinuteIsMandatory
                    val endTime = LocalDateTime.of(
                        year.toInt(),
                        month.toInt(),
                        day.toInt(),
                        endHour.toInt(),
                        endMinute.toInt()
                    )
                    if(endTime < event.startTime){
                        errorMessage += eventCannotEndBeforeItStarts
                    }
                    val newEvent = Event(
                        event.id,
                        title,
                        event.date,
                        event.startTime,
                        endTime,
                        description,
                        location,
                        teacher,
                        program
                    )
                    var counter = 0
                    allEvents?.forEach {loopEvent ->
                        if(loopEvent.startTime.dayOfMonth.toString() == day){
                            //make sure it starts and ends either before or after
                            var startsBefore = newEvent.startTime < loopEvent.startTime
                            var endsBefore = newEvent.endTime < loopEvent.startTime
                            var startsAfter = newEvent.startTime > loopEvent.endTime
                            var endsAfter = newEvent.endTime > loopEvent.endTime
                            if(!(startsBefore && endsBefore || startsAfter || endsAfter) && counter == 0 && loopEvent.id != newEvent.id){
                                errorMessage += "$selectedTimeIsAlreadyUsedByAnotherEvent (${loopEvent.title})."
                                counter++
                                //without the counter, the errorMessage can be appended many times.
                            }
                        }
                    }
                    if(errorMessage.isBlank()){
                        viewModel.updateEvent(newEvent)
                        navController.popBackStack()
                    }
                }){
                    Text(stringResource(R.string.save))
                }
                Button(onClick = {
                    allEvents?.forEach {loopEvent ->
                        if(loopEvent.startTime == event.startTime){
                            loopEvent.id?.let { viewModel.removeEvent(it) }
                        }
                    }
                    navController.popBackStack()
                }){
                    Text(stringResource(R.string.delete))
                }
            }
        } }
        item(){
            Text(errorMessage, color = Color.Red)
        }
        item{
            Column(){
                Text(
                    text = stringResource(R.string.title) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = title!!,
                    onValueChange = { title = it},
                    maxLines = 1,
                    readOnly = !editable,
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (editable) TextFieldDefaults.textFieldColors(containerColor = Color.White)
                    else TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)

                )
            }
            Column(){
                Text(
                    text = stringResource(R.string.description) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = description!!,
                    onValueChange = { description = it},
                    maxLines = 1,
                    readOnly = !editable,
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (editable) TextFieldDefaults.textFieldColors(containerColor = Color.White)
                    else TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)

                )
            }
            Column(){
                Text(
                    text = stringResource(R.string.location) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = location!!,
                    onValueChange = { location = it},
                    maxLines = 1,
                    readOnly = !editable,
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (editable) TextFieldDefaults.textFieldColors(containerColor = Color.White)
                    else TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)

                )
            }
            Column(){
                Text(
                    text = stringResource(R.string.day) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = day!!,
                    onValueChange = { day = it},
                    maxLines = 1,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)
                )
            }
            Column(){
                Text(
                    text = stringResource(R.string.month) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = month!!,
                    onValueChange = { month = it},
                    maxLines = 1,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)
                )
            }
            Column(){
                Text(
                    text = stringResource(R.string.year) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = year!!,
                    onValueChange = { year = it},
                    maxLines = 1,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)
                )
            }
            Column(){
                Text(
                    text = stringResource(R.string.teacher) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = teacher!!,
                    onValueChange = { teacher = it},
                    maxLines = 1,
                    readOnly = !editable,
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (editable) TextFieldDefaults.textFieldColors(containerColor = Color.White)
                    else TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)

                )
            }
            Column(){
                Text(
                    text = stringResource(R.string.program) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = program!!,
                    onValueChange = { program = it},
                    maxLines = 1,
                    readOnly = !editable,
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (editable) TextFieldDefaults.textFieldColors(containerColor = Color.White)
                    else TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)

                )
            }
        }
        item{
            Column(){
                Text(
                    text = stringResource(R.string.start_hour) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = startHour!!,
                    onValueChange = { startHour = it},
                    maxLines = 1,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)
                )
            }
            Column(){
                Text(
                    text = stringResource(R.string.start_minute) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = startMinute!!,
                    onValueChange = { startMinute = it},
                    maxLines = 1,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)
                )
            }
            Column(){
                Text(
                    text = stringResource(R.string.end_hour) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = endHour!!,
                    onValueChange = { endHour = it},
                    maxLines = 1,
                    readOnly = !editable,
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (editable) TextFieldDefaults.textFieldColors(containerColor = Color.White)
                    else TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)

                )
            }
            Column(){
                Text(
                    text = stringResource(R.string.end_minute) + ": ",
                    fontSize = 40.sp
                )
                TextField(
                    value = endMinute!!,
                    onValueChange = { endMinute = it},
                    maxLines = 1,
                    readOnly = !editable,
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (editable) TextFieldDefaults.textFieldColors(containerColor = Color.White)
                    else TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)

                )
            }
        }
    }
}