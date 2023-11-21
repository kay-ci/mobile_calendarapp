package com.example.calendarapp.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.calendarapp.domain.Event
import com.example.calendarapp.R
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



@RequiresApi(Build.VERSION_CODES.O)
val EventFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")


@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun ViewPage(
    navController: NavHostController,
    viewModel: CalendarViewModel
) {
    DailyPage(
        modifier = Modifier,
        navController = navController,
        viewModel = viewModel
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyPage(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: CalendarViewModel
){
    // Filter events based on the current date
    val filteredEvents = viewModel.getEventsForDate(LocalDate.parse(viewModel.selectedDate))
    val dayName = LocalDate.parse(viewModel.selectedDate).format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))
    Column(modifier = Modifier.background(Color.White)){
        NavigationBar(navController)
        DaySelect(modifier = modifier,dayName = dayName, viewModel)
        Spacer(modifier = Modifier.height(10.dp))
        IconButton(onClick = {
            navController.navigate(Routes.NewDayEventView.route)},
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = stringResource(R.string.add_button),
                modifier = Modifier.size(30.dp)
            )
        }
        DailyEventsTimeline(events = filteredEvents, modifier, navController)
    }
}
// Should be added at the top of every view to go to previous page.
@Composable
fun NavigationBar(navController: NavHostController) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .size(30.dp)
        .background(MaterialTheme.colorScheme.primary)
        .padding(5.dp),
    ){
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Go to previous page",
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyEventsTimeline(events: List<Event>, modifier: Modifier = Modifier, navController: NavHostController){
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
            ListEvents(events, navController = navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ListEvents(events: List<Event>, navController: NavHostController) {
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
            EventSpace(event = event, eventLength = eventLength, navController = navController)
            previousEndEvent = startEvent + eventDuration.toInt()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventSpace(event: Event, eventLength: Double, modifier: Modifier = Modifier, navController: NavHostController) {
    Card(
        modifier = modifier
            .height(eventLength.dp)
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(7.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        onClick = { navController.navigate(Routes.EditEventView.route + "/${event.startTime}") }
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
                text = "${event.startTime.format(EventFormatter)} - ${event.endTime.format(
                    EventFormatter
                )}",
                color = Color.Black
            )
        }
    }
}

@Composable
fun DaySelect(modifier: Modifier = Modifier, dayName: String, viewModel: CalendarViewModel){
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){
        IconButton(onClick = {
            viewModel.setDate(LocalDate.parse(viewModel.selectedDate).minusDays(1).toString())
        }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.left_arrow)
            )
        }
        Text(
            text = dayName,
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        IconButton(onClick = { viewModel.setDate(LocalDate.parse(viewModel.selectedDate).plusDays(1).toString())}) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = stringResource(R.string.right_arrow)
            )
        }
    }
}