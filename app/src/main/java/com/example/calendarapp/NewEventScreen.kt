package com.example.calendarapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.calendarapp.ui.theme.CalendarAppTheme
import java.time.LocalDateTime

@Composable
fun NewEventScreen(navController: NavHostController) {
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
            Field(true, "start", "")
            Field(true, "end", "")
            Field(true, "description", "")
        }
    }
}