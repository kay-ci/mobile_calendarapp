package com.example.calendarapp.presentation
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.calendarapp.domain.Event
import com.example.calendarapp.R
import com.example.calendarapp.domain.ForecastData
import com.example.calendarapp.domain.WeatherData
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


val EventFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")


@Composable

fun ViewPage(
    navController: NavHostController,
    viewModel: CalendarViewModel,
    lat: Double,
    lon: Double,
    forecastWeatherData: ForecastData?,
    weatherData: WeatherData?,
    lastUpdateTime: LocalDateTime
) {
    DailyPage(
        modifier = Modifier,
        navController = navController,
        viewModel = viewModel,
        lat = lat,
        lon = lon,
        forecastWeatherData,
        weatherData,
        lastUpdateTime
    )
}


@Composable
fun DailyPage(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: CalendarViewModel,
    lat: Double,
    lon: Double,
    forecastWeatherData: ForecastData?,
    weatherData: WeatherData?,
    lastUpdateTime: LocalDateTime
){
    // Filter events based on the current date
    viewModel.getEventsForDate(LocalDate.parse(viewModel.selectedDate))
    val events by viewModel.searchResults.observeAsState(listOf())
    val dayName = LocalDate.parse(viewModel.selectedDate).format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))

    Column(modifier = Modifier.background(Color.White)){
        NavigationBar(navController)
        DaySelect(modifier,dayName, viewModel, lat, lon, navController, forecastWeatherData, weatherData, lastUpdateTime)
        Spacer(modifier = Modifier.height(10.dp))
        IconButton(onClick = {
            navController.navigate(Routes.NewEventView.route+"/day")},
            modifier = Modifier
                .align(Alignment.End)
                .testTag("ADD_EVENT_BUTTON")
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = stringResource(R.string.add_button),
                modifier = Modifier.size(30.dp)
            )
        }
        DailyEventsTimeline(events = events, modifier, navController, viewModel)
    }
}
// Should be added at the top of every view to go to previous page.
@Composable
fun NavigationBar(navController: NavHostController) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .size(50.dp)
        .background(MaterialTheme.colorScheme.primary),
    ){
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.testTag("BACK_BUTTON")) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.go_to_previous_page),
                modifier = Modifier.align(Alignment.CenterStart)

            )
        }
    }
}
@Composable
fun DailyEventsTimeline(
    events: List<Event>,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: CalendarViewModel
) {
    val eventList = events ?: emptyList()
    val currentDay = LocalDate.parse(viewModel.selectedDate)
    val filteredEvents = eventList.filter { event ->
        event.startTime.toLocalDate() == currentDay
    }
    viewModel.getHolidaysForDate(currentDay)
    val holidays by viewModel.dayHolidays.observeAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Row {
            //Column for displaying the time
            Column {
                //Display Holiday Name
                holidays?.forEach { holiday ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 2.dp, horizontal = 4.dp)
                            .fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp)
                                .padding(5.dp)
                        ) {
                            Text(
                                text = holiday.name,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                //Loop over all the hours in a day to display them
                for (hour in (0..23)) {
                    Text(
                        text = "${hour}:00",
                        modifier = modifier
                            .padding(bottom = 10.dp, end = 10.dp)
                            .height(30.dp),
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }
            }
            ListEvents(filteredEvents, navController = navController)
        }
    }
}

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
fun DaySelect(
    modifier: Modifier = Modifier,
    dayName: String,
    viewModel: CalendarViewModel,
    lat: Double,
    lon: Double,
    navController: NavHostController,
    forecastWeatherData: ForecastData?,
    weatherData: WeatherData?,
    lastUpdateTime: LocalDateTime
){
    Row(modifier = modifier
        .fillMaxWidth(),
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
        CurrentDayWeather(navController, viewModel , lat, lon, forecastWeatherData, weatherData, lastUpdateTime)
        IconButton(onClick = { viewModel.setDate(LocalDate.parse(viewModel.selectedDate).plusDays(1).toString())}) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = stringResource(R.string.right_arrow)
            )
        }
    }
}
@Composable
fun CurrentDayWeather(
    navController: NavHostController,
    viewModel: CalendarViewModel,
    lat: Double,
    lon: Double,
    forecastWeatherData: ForecastData?,
    weatherData: WeatherData?,
    lastUpdateTime: LocalDateTime
){
    val selectedDay = viewModel.selectedDate
    if(selectedDay == LocalDate.now().toString()){
        Box (modifier = Modifier.clickable {
            navController.navigate(Routes.WeatherForecast.route)
        }){
            Text(
                text = "${lastUpdateTime.format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss"))}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            weatherData?.let {
                Text(
                    text = "${it.main?.temp?.roundToInt()}°C",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                it.weather?.get(0)?.icon.let { iconCode ->
                    val iconUrl = "https://openweathermap.org/img/wn/$iconCode.png"
                    Image(
                        painter = rememberImagePainter(iconUrl),
                        contentDescription = "Weather Icon",
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        }
    }
    else {
        // Filter weather entries for the selected date
        val filteredWeatherEntries = forecastWeatherData?.list
            ?.filter { selectedDay == it.dt_txt?.substring(0, 10) }
        Row(){
            // Check if there are any entries for the selected date
            if (!filteredWeatherEntries.isNullOrEmpty()) {
                // Display the temperature
                val temperature = filteredWeatherEntries[0].main?.temp
                Text(
                    text = "${temperature?.roundToInt()}°C",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                // Display the weather icon for the first entry
                filteredWeatherEntries[0].weather?.get(0)?.icon.let { iconCode ->
                    val iconUrl = "https://openweathermap.org/img/wn/$iconCode.png"
                    Image(
                        painter = rememberImagePainter(iconUrl),
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}