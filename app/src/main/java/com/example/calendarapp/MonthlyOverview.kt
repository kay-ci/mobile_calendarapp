package com.example.calendarapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.foundation.layout.PaddingValues as PaddingValues1


@Composable
@Preview
fun ShowMonthView(){
    MonthView()
}

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
@Composable
fun MonthView(){
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(Color.White)
    ) {
        val calendarViewModel = CalendarViewModel()
        Header(data = calendarViewModel)
        WeekContent(data = calendarViewModel)
    }
}

// The content inside each cell
@Composable
fun ContentItem(weekDay: String){
    Card(
        modifier = Modifier
            .padding(vertical = 2.dp, horizontal = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
    ) {
        Column(modifier = Modifier
            .width(40.dp)
            .height(35.dp)
            .padding(2.dp)
        ) {
            Text(
                text = weekDay,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun WeekContent(data: CalendarViewModel){
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        // content padding
        contentPadding = PaddingValues1(
            start = 10.dp,
            top = 10.dp,
            end = 10.dp,
        ),
        content = {
            val list = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            items(items = list) {date ->
                ContentItem(weekDay = date)
            }
        }
    )
}