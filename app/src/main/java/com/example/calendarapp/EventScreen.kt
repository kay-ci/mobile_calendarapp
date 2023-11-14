package com.example.calendarapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.TextStyle

@Composable
fun EventScreen(event: Event){
    var editable: Boolean by rememberSaveable{mutableStateOf(false)}
    LazyColumn{
        item{
            Row(){
                Button(onClick = {}){
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = "back arrow"
                    )
                }
                Button(onClick = {editable = !editable},
                    colors = if (editable) ButtonDefaults.buttonColors(Color.DarkGray) else ButtonDefaults.buttonColors()){
                    Text("Edit")
                }
                Button(onClick = {}){
                    Text("Save")
                }
            }
            Field(editable, "Title", event.title)
            Field(editable, "start", ""+event.startTime);
            Field(editable, "end", ""+event.endTime);
            Field(editable, "Description", event.description)
        }
    }
}

//Clean data to avoid malicious inputs
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Field(editable: Boolean, label: String, startingValue: String){
    var text by rememberSaveable { mutableStateOf(startingValue) }
    Column(){
        Text(
            text = "$label: ",
            fontSize = 40.sp
        )
        TextField(
            value = text,
            onValueChange = { text = it},
            maxLines = 1,
            readOnly = !editable,
            modifier = Modifier.fillMaxWidth(),
            colors = if (editable) TextFieldDefaults.textFieldColors(containerColor = Color.White)
                else TextFieldDefaults.textFieldColors(containerColor = Color.LightGray)
        )
    }
}