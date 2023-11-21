package com.example.calendarapp.presentation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.calendarapp.domain.Event
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import java.time.LocalDate

@Composable
fun NavigationComponent(navController: NavHostController, viewModel: CalendarViewModel) {
    NavHost(navController = navController, startDestination = Routes.MonthView.route) {

        // Navigation graph destinations
        composable(Routes.MonthView.route){
            MonthView(navController, viewModel)
        }

        composable(Routes.DailyView.route + "/{selectedDate}"){
                backStackEntry -> val selectedDate = backStackEntry.arguments?.getString("selectedDate")
            if (selectedDate != null) {
                ViewPage(navController, LocalDate.parse(selectedDate), viewModel)
            }
        }
        composable(Routes.EditEventView.route+"/{startTime}"){
                backStackEntry -> val startTime = backStackEntry.arguments?.getString("startTime")
            var theEvent: Event? = null
            viewModel.events.forEach {
                    event ->
                if(event.startTime.toString() == startTime){
                    theEvent = event
                }
            }
            theEvent?.let { EventScreen(it, navController, viewModel) }
        }

        composable(Routes.NewMonthEventView.route + "/{month}"){
                backStackEntry -> val month = backStackEntry.arguments?.getString("month")
            NewMonthEventScreen(navController, viewModel)
        }

        composable(Routes.NewDayEventView.route + "/{date}"){
                backStackEntry -> val date = backStackEntry.arguments?.getString("date")
            if(date != null){
                NewDayEventScreen(navController, LocalDate.parse(date), viewModel)
            }
        }
    }

}