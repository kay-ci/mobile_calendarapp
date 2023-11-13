package com.example.calendarapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EventScreen(event: Event){
    var editable: Boolean by rememberSaveable{mutableStateOf(false)}
    Column() {
        Button(onClick = {editable = !editable}){
            Text("Edit")
        }
        if(editable){
            TitleField(event.title)
            TitleField("start: " + event.startTime)
            TitleField("end: " + event.endTime)
            TitleField("description: " + event.description)
        }
        else{
            Text(event.title)
            Text("start: " + event.startTime)
            Text("end: " + event.endTime)
            Text("description: " + event.description)
        }
    }
}

//Clean data to avoid malicious inputs
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleField(startingText: String){
    var text by rememberSaveable { mutableStateOf(startingText) }
    Row(){
        Text(
            text = "Title: ",
            fontSize = 40.sp
        )
        TextField(
            value = text,
            onValueChange = { text = it;},//save to database as well
            maxLines = 1
        )

    }
}