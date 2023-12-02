package com.example.calendarapp

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calendarapp.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import com.example.calendarapp.presentation.NavigationComponent
import com.example.calendarapp.presentation.Routes
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.Month


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NavigationTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var navController: TestNavHostController
    private lateinit var testViewModel: CalendarViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current).apply{
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            testViewModel = hiltViewModel()

            NavigationComponent(navController = navController, viewModel = testViewModel)
        }
    }

    @Test
    fun testNavigationMonthToDailyView(){
        composeTestRule.apply{
            onNodeWithText("1").performClick()

            waitForIdle()
            val currentRoute = navController.currentDestination?.route
            assertEquals(currentRoute, Routes.DailyView.route + "")
        }
    }
    @Test
    fun testDailyToMonthView(){
        composeTestRule.apply {
            // Navigating to DailyView
            onNodeWithText("1").performClick()
            waitForIdle()

            // Going back
            onNodeWithTag("BACK_BUTTON").performClick()

            val currentRoute = navController.currentDestination?.route
            assertEquals(currentRoute, Routes.MonthView.route)
        }
    }

    @Test
    fun testMonthToAddEventView(){
        composeTestRule.apply {
            onNodeWithTag("create_event_view").performClick()
            waitForIdle()
            val currentRoute = navController.currentDestination?.route
            assertEquals(currentRoute, Routes.NewMonthEventView.route + "/{month}")
        }
    }

    @Test
    fun testDailyToAddEventView(){
        composeTestRule.apply {
            // Navigating to DailyView
            onNodeWithText("1").performClick()
            onNodeWithTag("ADD_EVENT_BUTTON").performClick()
            waitForIdle()

            val currentRoute = navController.currentDestination?.route
            assertEquals(currentRoute, Routes.NewDayEventView.route)
        }
    }

    @Test
    fun testNewDavEventToDaily(){
        composeTestRule.apply {
            // Navigating to add event view
            onNodeWithText("1").performClick()
            onNodeWithTag("ADD_EVENT_BUTTON").performClick()
            onNodeWithTag("BACK_BUTTON").performClick()

            val currentRoute = navController.currentDestination?.route
            assertEquals(currentRoute, Routes.DailyView.route)
        }
    }

}