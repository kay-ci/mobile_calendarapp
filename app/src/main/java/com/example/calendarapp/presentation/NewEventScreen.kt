package com.example.calendarapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.calendarapp.R
import java.time.LocalDate
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


@Composable
fun NewDayEventScreen(navController: NavHostController, date: LocalDate) {
    val currentDate = date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
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
            Field(false, "Day", currentDate);
            //Field(false, "Month", month)
            Field(true, "Year", "")
            Field(true, "Hour", "")
            Field(true, "Minute", "")
        }
    }
}