package com.example.calendarapp.presentation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import java.time.LocalDate

@Composable
fun NavigationComponent(navController: NavHostController, viewModel: CalendarViewModel) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val arguments = navBackStackEntry?.arguments
    val selectedDate: LocalDate? = arguments?.getString("selectedDate")?.let {
        LocalDate.parse(it)
    }
    NavHost(navController = navController, startDestination = Routes.MonthView.route) {

        // Navigation graph destinations
        composable(Routes.MonthView.route){
            MonthView(navController, viewModel)
        }

        composable(Routes.DailyView.route){
            if (selectedDate != null) {
                ViewPage(navController, selectedDate, viewModel)
            }
        }
        composable(Routes.EditEventView.route){
            EventView(navController)
        }

        composable(Routes.NewMonthEventView.route + "/{month}"){
                backStackEntry -> val month = backStackEntry.arguments?.getString("month")
            NewMonthEventScreen(navController, month)
        }

        composable(Routes.NewDayEventView.route){
            NewDayEventScreen(navController, 0, 1)
        }
    }

}