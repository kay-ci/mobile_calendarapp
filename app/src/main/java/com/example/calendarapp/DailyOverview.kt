package com.example.calendarapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime

@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun ViewPage(){
    DailyPage(modifier = Modifier, dayName = "Thursday November 9th, 2023")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyPage(modifier: Modifier, dayName : String){
    Column(modifier = Modifier.background(Color.White)){
        DaySelect(modifier = modifier,dayName = dayName)
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = painterResource(R.drawable.add_button),
            contentDescription = stringResource(R.string.add_button),
            modifier = modifier.align(Alignment.End)
        )
        val event1 = Event("Cooking",
            LocalDateTime.parse("2023-11-11T00:00:00"),
            LocalDateTime.parse("2023-11-11T03:30:00"),
            "Going to cook")
        val event2 = Event("Skiing",
            LocalDateTime.parse("2023-11-11T04:00:00"),
            LocalDateTime.parse("2023-11-11T06:30:00"),
            "Going to ski")
        DailyEventsTimeline(listOf<Event>(event1,event2))
    }
}

@Composable
private fun DailyEventsTimeline(events: List<Event>,  modifier: Modifier = Modifier){
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .fillMaxWidth()) {
        Row{
            //Column for displaying the time
            Column {
                //Loop over all the hours in a day to display them
                for(hour in (0..23)){
                    Text(text = "${hour.toString()}:00",
                        modifier = modifier
                            .padding(12.dp),
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }
            //Column for the displaying the events
            Column {

            }
        }
    }
}

@Composable
private fun DaySelect(modifier: Modifier = Modifier, dayName: String){
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){
        Image(
            painter = painterResource(R.drawable.left_arrow),
            contentDescription = stringResource(R.string.left_arrow),
            modifier = modifier.size(30.dp)
        )
        Text(
            text = dayName,
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Image(
            painter = painterResource(R.drawable.right_arrow),
            contentDescription = stringResource(R.string.right_arrow),
            modifier = modifier.size(30.dp)
        )
    }
}