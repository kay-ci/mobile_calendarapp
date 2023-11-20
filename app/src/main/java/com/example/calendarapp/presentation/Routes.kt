package com.example.calendarapp.presentation

sealed class Routes(val route: String){
    object MonthView : Routes("monthView")
    object DailyView : Routes("dailyView/{selectedDate}")
    object EditEventView : Routes("editEventView")
    object NewMonthEventView: Routes("newMonthEventView")
    object NewDayEventView: Routes("newDayEventView")
}
