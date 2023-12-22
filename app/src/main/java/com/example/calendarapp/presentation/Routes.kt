package com.example.calendarapp.presentation

sealed class Routes(val route: String){
    object MonthView : Routes("monthView")
    object DailyView : Routes("dailyView")
    object EditEventView : Routes("editEventView")
    object NewEventView: Routes("newEventView")
    object WeatherForecast : Routes("weatherDetail")

}
