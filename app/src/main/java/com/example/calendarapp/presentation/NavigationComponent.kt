package com.example.calendarapp.presentation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

        composable(Routes.DailyView.route){
            ViewPage(navController, viewModel)
        }
        composable(Routes.EditEventView.route + "/{startTime}") { backStackEntry ->
            val startTime = backStackEntry.arguments?.getString("startTime")

            // Observe the LiveData using observeAsState
            val allEvents by viewModel.allEvents.observeAsState()

            var theEvent: Event? = null

            // Check if allEvents is not null before iterating
            allEvents?.forEach { event ->
                if (event.startTime.toString() == startTime) {
                    theEvent = event
                }
            }

            theEvent?.let { EventScreen(it, navController, viewModel) }
        }

        composable(Routes.NewMonthEventView.route + "/{month}"){
                backStackEntry -> val month = backStackEntry.arguments?.getString("month")
            NewMonthEventScreen(navController, viewModel)
        }

        composable(Routes.NewDayEventView.route){
            NewDayEventScreen(navController, viewModel)
        }
    }

}