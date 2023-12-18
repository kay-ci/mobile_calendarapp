package com.example.calendarapp.presentation
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.calendarapp.domain.Holiday
import com.example.calendarapp.domain.getDay
import com.example.calendarapp.domain.getMonth
import com.example.calendarapp.domain.getYear
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.google.gson.JsonArray
import org.json.JSONArray
import java.time.LocalDate
import java.time.Month
import kotlin.math.roundToInt
import androidx.compose.foundation.layout.PaddingValues as PaddingValues1


@Composable
fun MonthView(
    navController: NavHostController,
    viewModel: CalendarViewModel,
    lat: Double,
    lon: Double
) {
    val holidayData by rememberSaveable { viewModel.holidayData }
    // Fetch data
    if(holidayData == "" ) {
        viewModel.fetchHolidayData()
        viewModel.getHolidaysFromFile()
        viewModel.getAllHolidays()
    }

    // This uses the fetched data and it works!
    val gson = GsonBuilder().setPrettyPrinting().create()
    val prettyJson = gson.toJson(JsonParser().parse(holidayData))


    Column (modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        val weatherData by viewModel.weatherData.observeAsState()
        // Fetch weather data when the page is loaded
        DisposableEffect(Unit) {
            viewModel.fetchWeatherData(lat.toString(), lon.toString())
            onDispose {}
        }
        Row(){
            weatherData?.let {
                if (it != null) {
                    Text(
                        text = "${it.main?.temp?.roundToInt()}",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
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


        val list = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        Header(data = viewModel)
        WeekDaysHeader(list = list, navController, viewModel)
        MonthContent(data = viewModel, list = viewModel.daysInMonth.value, navController)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ){
            IconButton(onClick = {
                navController.navigate(Routes.NewMonthEventView.route + "/${viewModel.currentMonth.value}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_event_view")
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "AddEvent",
                    modifier = Modifier.size(40.dp)
                )
            }
//            Text(text = prettyJson)
        }

    }
}




// Display month, year and buttons
@Composable
fun Header(data: CalendarViewModel){
    Row(modifier = Modifier
        .padding(horizontal = 16.dp)
    ){

        IconButton(onClick = { data.previousMonth() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Previous Month"
            )
        }

        Text(
            text = "${data.currentMonth.value} ${data.currentYear.value}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )

        IconButton(onClick = { data.nextMonth() }) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Next Month"
            )
        }
    }
}

// Display the 7 weeks days
@Composable
fun WeekDaysHeader(list : List<String>, navController: NavHostController, viewModel: CalendarViewModel){
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        // content padding
        contentPadding = PaddingValues1(
            start = 10.dp,
            top = 10.dp,
            end = 10.dp,
        ),
        content = {

            items(items = list) {cellContent ->
                ContentItem(content = cellContent, navController = navController, 0, "", viewModel)
            }
        }
    )
}

// Display the month days
@Composable
fun MonthContent(data: CalendarViewModel, list: List<String>, navController: NavHostController){
    val firstDayOfWeek = data.firstWeekDay.value
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        // content padding
        contentPadding = PaddingValues1(
            start = 10.dp,
            top = 10.dp,
            end = 10.dp,
        ),
        content = {
            val offsetList = mutableListOf<String>()

            // Adding empty cells for offset
            // handle off by one
            repeat(if (firstDayOfWeek == 1) 0 else firstDayOfWeek + 1) {
                offsetList.add("") // or any placeholder value for empty cells
            }

            // Add the rest of the month days
            offsetList.addAll(list)
            items(items = offsetList) {cellContent ->
                ContentItem(content = cellContent, navController,
                    currentYear = data.currentYear.value,
                    currentMonth = data.currentMonth.value, data)
            }
        }
    )
}

// The content inside each cell
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentItem(content: String, navController: NavHostController, currentYear: Int,
                currentMonth: String, viewModel: CalendarViewModel){
    var holidayName = ""
    if(content.isNotBlank()) {
        var theColor = MaterialTheme.colorScheme.inversePrimary
        var today = LocalDate.now()


        val allEvents by viewModel.allEvents.observeAsState()
        allEvents?.forEach {loopEvent ->
            val sameYear = loopEvent.startTime.year == currentYear
            val sameMonth = loopEvent.startTime.monthValue == viewModel.getMonthNumber(currentMonth)
            val sameDay = loopEvent.startTime.dayOfMonth.toString() == content
            if(sameYear && sameMonth && sameDay){
                theColor = MaterialTheme.colorScheme.secondary
            }
        }

        //current day has to be differently coloured
        if(today.year == currentYear && today.month.toString() == currentMonth.uppercase() && today.dayOfMonth.toString() == content){
            theColor = MaterialTheme.colorScheme.tertiary
        }
        Card(
            modifier = Modifier
                .padding(vertical = 2.dp, horizontal = 4.dp),
            colors = (CardDefaults.cardColors(containerColor = theColor)),
            onClick = {
                if(currentYear !=0 && currentMonth != ""){
                    // Parse the selected day to LocalDate
                    val selectedDate = LocalDate.of(
                        currentYear,
                        Month.valueOf(currentMonth.uppercase()).value, // Use Month enum to get the month value
                        content.toInt()
                    )

                    viewModel.setDate(selectedDate.toString())

                    // Pass the selected date to DailyView
                    navController.navigate(Routes.DailyView.route) {
                        launchSingleTop = true
                        popUpTo(Routes.MonthView.route) { saveState = true }
                    }
                }
            }

        ) {
            Column(
                modifier = Modifier
                    .width(40.dp)
                    .height(35.dp)
                    .padding(2.dp)
            ) {
                Text(
                    text = content,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}