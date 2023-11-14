package com.example.calendarapp

sealed class Routes(val route: String){
    object MonthView : Routes("monthView")
    object DailyView : Routes("dailyView")
    object EditEventView : Routes("editEventView")
    object CreateEventView :Routes("createEventView")
}