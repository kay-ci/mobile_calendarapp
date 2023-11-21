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
import java.time.LocalDate
import java.time.LocalDateTime


//
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventView(navController: NavHostController) {
    val testEvent = Event("Skiing",
        LocalDate.parse("2023-11-11"),
        LocalDateTime.parse("2023-11-11T04:00:00"),
        LocalDateTime.parse("2023-11-11T06:30:00"),
        "Going to ski","Mont Bruno")
    EventScreen(event = testEvent, navController)
}

//Clean data to avoid malicious inputs
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Field(editable: Boolean, label: String?, startingValue: String?){
    var text by rememberSaveable { mutableStateOf(startingValue) }
    Column(){
        Text(
            text = "$label: ",
            fontSize = 40.sp
        )
        TextField(
            value = text!!,
            onValueChange = { text = it},
            maxLines = 1,
            readOnly = !editable,
            modifier = Modifier.fillMaxWidth(),
            colors = if (editable) TextFieldDefaults.textFieldColors(containerColor = Color.White)
            else TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)
        )
    }
}

@Composable
fun EventScreen(event: Event, navController: NavHostController){
    var editable: Boolean by rememberSaveable{mutableStateOf(false)}
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
                    /* TODO validation on the fields and then go to previous page*/
                    navController.popBackStack()
                }){
                    Text("Save")
                }
                Button(onClick = {
                    navController.popBackStack()
                }){
                    Text("Delete")
                }
            }
        } }
        item{
            Field(editable, "Title", event.title)
            Field(editable, "Description", event.description)
            Field(editable, "Location", event.location)
        }
        item{
            Text("Start Time:", fontSize = 40.sp)
            Field(editable, "Day", event.startTime.dayOfMonth.toString())
            Field(editable, "Month", event.startTime.month.toString())
            Field(editable, "Year", event.startTime.year.toString())
            Field(editable, "Hour", event.startTime.hour.toString())
            Field(editable, "Minute", event.startTime.minute.toString())
        }
        item{
            Text("End Time:", fontSize = 40.sp)
            Field(editable, "Day", event.endTime.dayOfMonth.toString())
            Field(editable, "Month", event.endTime.month.toString())
            Field(editable, "Year", event.endTime.year.toString())
            Field(editable, "Hour", event.endTime.hour.toString())
            Field(editable, "Minute", event.endTime.minute.toString())
        }
    }
}


// Create event view maybe put in another file
@Composable
fun CreateEvent(navController: NavHostController){
    NavigationBar(navController = navController)
}