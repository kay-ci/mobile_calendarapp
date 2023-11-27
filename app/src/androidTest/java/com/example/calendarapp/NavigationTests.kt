package com.example.calendarapp

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
import com.example.calendarapp.presentation.ViewPage
import com.example.calendarapp.presentation.viewmodel.CalendarViewModel
import com.example.calendarapp.ui.theme.CalendarAppTheme
import org.junit.Assert
import org.junit.Test

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NavigationTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private lateinit var navController: NavHostController
    private lateinit var testViewModel: CalendarViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            navController = rememberNavController()
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
            Assert.assertEquals(currentRoute, Routes.DailyView.route + "")
        }
    }

//    @Test
//    fun testDailyToMonthView(){
//        composeTestRuleOthers.setContent {
//            CalendarAppTheme{
//                ViewPage(navController = navController, viewModel = testViewModel)
//            }
//        }
//        composeTestRuleOthers.onNodeWithTag("back_button").performClick()
//        composeTestRuleOthers.waitForIdle()
//
//            val currentRoute = navController.currentDestination?.route
//            Assert.assertEquals(currentRoute, Routes.MonthView.route)
//
//    }

    @Test
    fun testMonthToAddEventView(){
        composeTestRule.apply {
            onNodeWithTag("create_event_view").performClick()
            waitForIdle()
            val currentRoute = navController.currentDestination?.route
            Assert.assertEquals(currentRoute, Routes.NewMonthEventView.route + "/{month}")
        }
    }
}