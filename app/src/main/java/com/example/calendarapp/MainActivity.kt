package com.example.calendarapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calendarapp.ui.theme.CalendarAppTheme

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
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.MonthView.route) {

            // Navigation graph destinations
            composable(Routes.MonthView.route){
                MonthView(navController)
            }

            composable(Routes.DailyView.route){
                ViewPage()
            }

            composable(Routes.EditEventView.route){
                EventView(navController)
            }

            composable(Routes.CreateEventView.route){
                CreateEvent()
            }
        }

    }
}
