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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
                        contentDescription = "back arrow"
                    )
                }
                Button(onClick = {editable = !editable},
                    colors = if (editable) ButtonDefaults.buttonColors(Color.DarkGray) else ButtonDefaults.buttonColors()){
                    Text("Edit")
                }
                Button(onClick = {
                    var eventFromVM: Event? = null
                    viewModel.events.forEach{vmEvent ->
                        if(vmEvent.startTime == event.startTime){
                            eventFromVM = vmEvent
                        }
                    }
                    try{
                        val year1 = year
                        val month1 = month
                        val day1 = day
                        val endTime = LocalDateTime.of(year.toInt(), month.toInt(), day.toInt(), endHour.toInt(), endMinute.toInt())
                        val newEvent = Event(title, description = description, location = location, date = event.date, startTime = event.startTime, endTime = endTime)
                        eventFromVM?.title = newEvent.title
                        eventFromVM?.description = newEvent.description
                        eventFromVM?.location = newEvent.location
                        eventFromVM?.endTime = newEvent.endTime
                        navController.popBackStack()
                    }
                    catch(e: Exception){
                        //show error
                    }
                }){
                    Text("Save")
                }
                Button(onClick = {
                    viewModel.events.forEachIndexed{index, loopEvent ->
                        if(loopEvent.startTime == event.startTime){
                            viewModel.removeEvent(index)
                        }
                    }
                    navController.popBackStack()
                }){
                    Text("Delete")
                }
            }
        } }
        item{
            Column(){
                Text(
                    text = "Title: ",
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
                    text = "Description: ",
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
                    text = "Location: ",
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
                    text = "Day: ",
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
                    text = "Month: ",
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
                    text = "Year: ",
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
        }
        item{
            Column(){
                Text(
                    text = "Start Hour: ",
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
                    text = "Start Minute: ",
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
                    text = "End Hour: ",
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
                    text = "End Minute: ",
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