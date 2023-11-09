package com.example.calendarapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable

fun ViewPage(){
    DailyPage(modifier = Modifier)
}
@Composable
fun DailyPage(modifier: Modifier){
    Column{
        DaySelect(modifier = modifier)
        Text(text = "+", modifier = modifier.align(Alignment.End))
    }
}

@Composable
fun DaySelect(modifier: Modifier = Modifier){
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){
        Text("<--")
        Text(
            text = "9 November 2023",
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text("-->")
    }
}