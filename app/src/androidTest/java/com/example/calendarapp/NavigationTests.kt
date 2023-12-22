package com.example.calendarapp

import android.app.Application
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
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
import org.junit.Test


@RunWith(AndroidJUnit4::class)
class NavigationTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var application: Application
    private lateinit var testViewModel: CalendarViewModel

    @Before
    fun setup() {

        composeTestRule.setContent {
            val owner = LocalViewModelStoreOwner.current
            owner?.let {
                testViewModel = viewModel(
                    it,
                    "CalenderViewModel",
                    MainActivity.ViewModelFactory(
                        LocalContext.current.applicationContext
                                as Application
                    )
                )
                navController = TestNavHostController(LocalContext.current).apply {
                    navigatorProvider.addNavigator(ComposeNavigator())
                }

                val forecastWeatherData by testViewModel.weatherDataForecast.observeAsState()
                application = ApplicationProvider.getApplicationContext<Application>()

                NavigationComponent(
                    navController = navController,
                    viewModel = testViewModel,
                    0.0,
                    0.0,
                    forecastWeatherData
                )
            }
        }
    }

    @Test
    fun testNavigationMonthToDailyView(){
        composeTestRule.apply{
            onNodeWithText("1\n").assertIsDisplayed()
            onNodeWithText("1\n").performClick()
            waitForIdle()
            val currentRoute = navController.currentDestination?.route
            assertEquals(Routes.DailyView.route + "", currentRoute )
        }
    }
    @Test
    fun testDailyToMonthView(){
        composeTestRule.apply {
            // Navigating to DailyView
            waitForIdle()
            onNodeWithText("1\n", useUnmergedTree = true).assertIsDisplayed()
            onNodeWithText("1\n", useUnmergedTree = true).performClick()
            waitForIdle()

            // Going back
            onNodeWithTag("BACK_BUTTON").performClick()

            val currentRoute = navController.currentDestination?.route
            assertEquals(Routes.MonthView.route, currentRoute)
        }
    }

    @Test
    fun testMonthToAddEventView(){
        composeTestRule.apply {
            onNodeWithTag("create_event_view").performClick()
            waitForIdle()
            val currentRoute = navController.currentDestination?.route
            assertEquals(Routes.NewEventView.route + "/{option}", currentRoute )
        }
    }

    @Test
    fun testDailyToAddEventView(){
        composeTestRule.apply {
            // Navigating to DailyView
            onNodeWithText("1\n").performClick()
            onNodeWithTag("ADD_EVENT_BUTTON").performClick()
            waitForIdle()

            val currentRoute = navController.currentDestination?.route
            assertEquals(Routes.NewEventView.route + "/{option}", currentRoute)
        }
    }

    @Test
    fun testNewDavEventToDaily(){
        composeTestRule.apply {
            // Navigating to add event view
            onNodeWithText("1\n").performClick()
            onNodeWithTag("ADD_EVENT_BUTTON").performClick()
            onNodeWithTag("BACK_BUTTON").performClick()

            val currentRoute = navController.currentDestination?.route
            assertEquals(Routes.DailyView.route, currentRoute)
        }
    }

}