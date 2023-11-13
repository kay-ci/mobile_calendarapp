package com.example.calendarapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
val EventFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun ViewPage(){
    val event2 = Event("Skiing",
        LocalDateTime.parse("2023-11-11T04:00:00"),
        LocalDateTime.parse("2023-11-11T06:30:00"),
        "Going to ski","Mont Bruno")
    DailyPage(modifier = Modifier, dayName = "Thursday November 9th, 2023")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyPage(modifier: Modifier, dayName : String){
    val events = remember { mutableStateListOf<Event>() }
    Column(modifier = Modifier.background(Color.White)){
        DaySelect(modifier = modifier,dayName = dayName)
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = painterResource(R.drawable.add_button),
            contentDescription = stringResource(R.string.add_button),
            modifier = modifier.align(Alignment.End)
                .clickable{/*Navigate to add event*/}
            )
        DailyEventsTimeline(events = events)
        }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyEventsTimeline(events: List<Event>,  modifier: Modifier = Modifier){
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .fillMaxWidth()) {
        Row{
            //Column for displaying the time
            Column {
                //Loop over all the hours in a day to display them
                for(hour in (0..23)){
                    Text(text = "${hour}:00",
                        modifier = modifier
                            .padding(bottom = 10.dp, end = 10.dp)
                            .height(30.dp),
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }
            }
            ListEvents(events)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ListEvents(events: List<Event>) {
    //Column for the displaying the events
    Column {
        // Sort events by start time to ensure accurate placement
        val sortedEvents = events.sortedBy { it.startTime }
        var previousEndEvent = 0
        for (event in sortedEvents) {
            val startEvent = event.startTime.hour * 60 + event.startTime.minute
            val eventDuration = Duration.between(event.startTime, event.endTime).toMinutes()
            val totalStartTime = (startEvent - previousEndEvent) * 41
            // Spacer for empty time slots before the event
            Spacer(
                modifier = Modifier
                    .height((totalStartTime / 60).dp)
                    .fillMaxWidth()
            )
            val eventLength = (eventDuration.toDouble() / 60) * 42
            EventSpace(event = event, eventLength = eventLength)
            previousEndEvent = startEvent + eventDuration.toInt()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventSpace(event: Event, eventLength: Double,modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(eventLength.dp)
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(7.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = event.title,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "${event.startTime.format(EventFormatter)} - ${event.endTime.format(EventFormatter)}",
                color = Color.Black
            )
        }
    }
}

@Composable
fun DaySelect(modifier: Modifier = Modifier, dayName: String){
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){
        Image(
            painter = painterResource(R.drawable.left_arrow),
            contentDescription = stringResource(R.string.left_arrow),
            modifier = modifier.size(40.dp)
                .clickable{/*Go to previous day*/}
        )
        Text(
            text = dayName,
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Image(
            painter = painterResource(R.drawable.right_arrow),
            contentDescription = stringResource(R.string.right_arrow),
            modifier = modifier.size(40.dp)
                .clickable{/*Go to next day*/}
        )
    }
}