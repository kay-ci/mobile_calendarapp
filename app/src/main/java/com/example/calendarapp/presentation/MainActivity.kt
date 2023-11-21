package com.example.calendarapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calendarapp.domain.Event
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import com.example.calendarapp.ui.theme.CalendarAppTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeView()
                }
            }
        }
    }

    @Composable
    fun HomeView(){
        val viewModel: CalendarViewModel = viewModel()

        val navController = rememberNavController()
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
}
