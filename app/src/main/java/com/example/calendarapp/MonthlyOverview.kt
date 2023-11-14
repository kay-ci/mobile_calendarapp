package com.example.calendarapp
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import java.time.LocalDateTime
import androidx.compose.foundation.layout.PaddingValues as PaddingValues1


@Composable
@Preview
fun ShowMonthView(){
    val navController = rememberNavController()
    MonthView(navController)
}

@Composable
fun MonthView(navController: NavHostController) {
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(Color.White)
    ) {
        val calendarViewModel : CalendarViewModel = viewModel()

        val list = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        Header(data = calendarViewModel)
        WeekDaysHeader(list = list)
        MonthContent(data = calendarViewModel, list = calendarViewModel.daysInMonth.value)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ){
            IconButton(onClick = {
                val event2 = Event("Skiing",
                    LocalDateTime.parse("2023-11-11T04:00:00"),
                    LocalDateTime.parse("2023-11-11T06:30:00"),
                    "Going to ski","Mont Bruno")
                navController.navigate(Routes.EditEventView.route) },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "AddEvent",
                    modifier = Modifier.size(40.dp)
                )
            }
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
fun WeekDaysHeader(list : List<String>){
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
                ContentItem(content = cellContent)
            }
        }
    )
}

// Display the month days
@Composable
fun MonthContent(data: CalendarViewModel, list : List<String>){
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
                ContentItem(content = cellContent)
            }
        }
    )
}

// The content inside each cell
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentItem(content: String){
    if(content.isNotBlank()) {
        Card(
            modifier = Modifier
                .padding(vertical = 2.dp, horizontal = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = { /*TODO*/ }
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