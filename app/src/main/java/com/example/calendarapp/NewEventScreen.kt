package com.example.calendarapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.calendarapp.ui.theme.CalendarAppTheme
import java.time.LocalDateTime

@Composable
fun NewEventScreen() {
    LazyColumn(){
        item(){
            Text("New Event",
                modifier = Modifier.fillMaxWidth()
                    .background(Color.Gray),
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                color = Color.White)
        }//use TimeDialog
        item(){
            Field(true, "Title", "")
            Field(true, "start", "")
            Field(true, "end", "")
            Field(true, "description", "")
        }
    }
}