package com.example.calendarapp

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.calendarapp.ui.theme.CalendarAppTheme
import java.time.LocalDate
import java.time.LocalDateTime

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
                    //MonthView()
                    val event2 = Event("Skiing",
                        LocalDate.parse("2023-11-11"),
                        LocalDateTime.parse("2023-11-11T04:00:00"),
                        LocalDateTime.parse("2023-11-11T06:30:00"),
                        "Going to ski","Mont Bruno")
                    NewMonthEvent(1)
                }
            }
        }
    }

    @Composable
    fun HomeView(){
        val viewModel: CalendarViewModel = viewModel()

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val arguments = navBackStackEntry?.arguments
        val selectedDate: LocalDate? = arguments?.getString("selectedDate")?.let {
            LocalDate.parse(it)
        }
        NavHost(navController = navController, startDestination = Routes.MonthView.route) {

            // Navigation graph destinations
            composable(Routes.MonthView.route){
                MonthView(navController)
            }

            composable(Routes.DailyView.route){
                if (selectedDate != null) {
                    ViewPage(navController, selectedDate, viewModel)
                }
            }

            composable(Routes.EditEventView.route){
                EventView(navController)
            }

            composable(Routes.CreateEventView.route){
                //CreateEvent(navController)
            }
        }

    }
}
