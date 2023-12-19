package com.example.calendarapp.presentation.viewmodel

import android.app.Application
import android.icu.util.Calendar
import android.icu.util.ULocale
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.data.DownloadData
import com.example.calendarapp.data.EventRepository
import com.example.calendarapp.data.EventRoomDatabase
import com.example.calendarapp.data.TempStorage
import com.example.calendarapp.data.WeatherRepository
import com.example.calendarapp.domain.Event
import com.example.calendarapp.domain.Holiday
import com.example.calendarapp.domain.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class CalendarViewModel (application: Application) : ViewModel() {
    var selectedDate by mutableStateOf("")
    fun setDate(newDate: String){
        selectedDate = newDate
    }
    val fetchedYear = mutableStateOf(0)
    // Holds application context
    private val appContext = application
    // Will hold data fetched from file
    var holidayData = mutableStateOf("")
    private val filename = "holidayData"

    private val calendar: Calendar = Calendar.getInstance(ULocale("en_US@calendar=gregorian"))

    private val _currentDay = mutableStateOf(0)
    val currentDay: MutableState<Int> = _currentDay

    private val _currentMonth = mutableStateOf("")
    val currentMonth: MutableState<String> = _currentMonth

    private val _currentYear = mutableStateOf(0)
    val currentYear: MutableState<Int> = _currentYear

    private val _daysInMonth = mutableStateOf<List<String>>(emptyList())
    val daysInMonth : MutableState<List<String>> = _daysInMonth

    private val _firstWeekDay = mutableStateOf<Int>(Calendar.SUNDAY)
    val firstWeekDay : MutableState<Int> = _firstWeekDay
    val allEvents : LiveData<List<Event>>
    private val repository : EventRepository
    val searchResults: MutableLiveData<List<Event>>

    val allHolidays: MutableLiveData<List<Holiday>> = MutableLiveData()
    val dayHolidays: MutableLiveData<List<Holiday>> = MutableLiveData()

    init {
        updateMonthYear()
        updateDaysOfMonth()
        updateFirstWeekDay()
        val eventDb = EventRoomDatabase.getInstance(application)
        val eventDao = eventDb.eventDao()
        repository = EventRepository(eventDao)

        allEvents = repository.allEvents
        searchResults = repository.searchResults
    }

    fun getMonthNumber(month: String): Int {
        return when (month) {
            "January" -> 1
            "February" -> 2
            "March" -> 3
            "April" -> 4
            "May" -> 5
            "June" -> 6
            "July" -> 7
            "August" -> 8
            "September" -> 9
            "October" -> 10
            "November" -> 11
            "December" -> 12
            else -> -1 // Return -1 for invalid input
        }
    }

    fun getMonthName(monthNumber: Int): String {
        return when (monthNumber) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Invalid month number"
        }
    }

    private fun updateMonthYear() {
        val month = when (calendar.get(Calendar.MONTH)){
            0 -> "January"
            1 -> "February"
            2 -> "March"
            3 -> "April"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "August"
            8 -> "September"
            9 -> "October"
            10 -> "November"
            11 -> "December"
            else -> throw Exception("Invalid Month")
        }
        _currentMonth.value = month
        _currentYear.value = calendar.get(Calendar.YEAR)
    }
    fun nextMonth(){
        calendar.add(Calendar.MONTH, 1)
        updateMonthYear()
        updateDaysOfMonth()
        updateFirstWeekDay()
    }

    fun previousMonth(){
        calendar.add(Calendar.MONTH, -1)
        updateMonthYear()
        updateDaysOfMonth()
        updateFirstWeekDay()
    }
    private fun updateDaysOfMonth(){
        val days = mutableListOf<String>()
        val totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (day in 1..totalDays) {
            days.add(day.toString())
        }

        _daysInMonth.value = days
    }

    private fun updateFirstWeekDay(){
        firstWeekDay.value = calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun getEventsForDate(date: LocalDate){
        repository.findEvent(date)
    }
    fun addEvent(event: Event){
        repository.insertEvent(event)
    }

    fun removeEvent(index: Int){
        repository.deleteEvent(index)
    }

    fun updateEvent(updatedEvent: Event) {
        repository.updateEvent(updatedEvent)
    }


    fun containsEvent(theEvent: Event): Boolean {
        var output: Boolean = false
        allEvents.value?.forEach { event ->
            if (event.startTime == theEvent.startTime) {
                output = true
            }
        }
        return output
    }

    private val weatherRepository = WeatherRepository()

    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData> get() = _weatherData

    fun fetchWeatherData(latitude: String, longitude: String) {
        weatherRepository.getCurrentWeatherData(latitude, longitude)
            .enqueue(object : Callback<WeatherData> {
                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    if (response.isSuccessful) {
                        viewModelScope.launch(Dispatchers.Main) {
                            _weatherData.value = response.body()!!
                        }
                    } else {
                        // Handle error
                        Log.e("Weather", "Error fetching weather data: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    // Handle failure
                    Log.e("Weather", "Failure while fetching weather data: ${t.message}")
                }
            })
    }

    fun fetchHolidayData(){
        //Create a coroutine to fetch the data
        viewModelScope.launch ( Dispatchers.IO ){
            val urlString = "https://date.nager.at/api/v3/PublicHolidays/${currentYear.value}/ca"
            fetchedYear.value = currentYear.value
            DownloadData(context = appContext).fetchData(filename, urlString)
        }
    }

    fun getHolidaysFromFile(){
        /*Fetch the data asynchronously via a coroutine. It needs to run on the
        Main thread, since the data is required by the Main thread.
         */
        viewModelScope.launch (Dispatchers.Main){
            val fileData = TempStorage(appContext).readDataFromFile(filename)
            holidayData.value = fileData
            println("Raw JSON String Data: $fileData")
            getAllHolidays()
        }

    }
    private fun getAllHolidays() {
        //val jsonStringHardCoded = "[{\"date\":\"2023-12-25\",\"localName\":\"Christmas Day\",\"name\":\"Christmas Day\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":true,\"counties\":null,\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2023-12-26\",\"localName\":\"Boxing Day\",\"name\":\"St. Stephen's Day\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":false,\"counties\":[\"CA-AB\",\"CA-NB\",\"CA-NS\",\"CA-ON\",\"CA-PE\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-01-01\",\"localName\":\"New Year's Day\",\"name\":\"New Year's Day\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":true,\"counties\":null,\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-02-19\",\"localName\":\"Louis Riel Day\",\"name\":\"Louis Riel Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-MB\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-02-19\",\"localName\":\"Islander Day\",\"name\":\"Islander Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-PE\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-02-19\",\"localName\":\"Heritage Day\",\"name\":\"Heritage Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-NS\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-02-19\",\"localName\":\"Family Day\",\"name\":\"Family Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-AB\",\"CA-BC\",\"CA-NB\",\"CA-ON\",\"CA-SK\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-03-17\",\"localName\":\"Saint Patrick's Day\",\"name\":\"Saint Patrick's Day\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":false,\"counties\":[\"CA-NL\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-03-29\",\"localName\":\"Good Friday\",\"name\":\"Good Friday\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":true,\"counties\":null,\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-04-01\",\"localName\":\"Easter Monday\",\"name\":\"Easter Monday\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-AB\",\"CA-PE\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-04-23\",\"localName\":\"Saint George's Day\",\"name\":\"Saint George's Day\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":false,\"counties\":[\"CA-NL\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-05-20\",\"localName\":\"National Patriots' Day\",\"name\":\"National Patriots' Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-QC\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-05-20\",\"localName\":\"Victoria Day\",\"name\":\"Victoria Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":true,\"counties\":null,\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-06-21\",\"localName\":\"National Aboriginal Day\",\"name\":\"National Aboriginal Day\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":false,\"counties\":[\"CA-NT\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-06-24\",\"localName\":\"Discovery Day\",\"name\":\"Discovery Day\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":false,\"counties\":[\"CA-NL\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-06-24\",\"localName\":\"Fête nationale du Québec\",\"name\":\"National Holiday\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":false,\"counties\":[\"CA-QC\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-07-01\",\"localName\":\"Canada Day\",\"name\":\"Canada Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":true,\"counties\":null,\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-07-12\",\"localName\":\"Orangemen's Day\",\"name\":\"Orangemen's Day\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":false,\"counties\":[\"CA-NL\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-08-05\",\"localName\":\"Civic Holiday\",\"name\":\"Civic Holiday\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-MB\",\"CA-NL\",\"CA-NT\",\"CA-NU\",\"CA-ON\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-08-05\",\"localName\":\"British Columbia Day\",\"name\":\"British Columbia Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-BC\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-08-05\",\"localName\":\"Heritage Day\",\"name\":\"Heritage Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-AB\",\"CA-YT\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-08-05\",\"localName\":\"New Brunswick Day\",\"name\":\"New Brunswick Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-NB\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-08-05\",\"localName\":\"Natal Day\",\"name\":\"Natal Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-NS\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-08-05\",\"localName\":\"Saskatchewan Day\",\"name\":\"Saskatchewan Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-SK\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-08-19\",\"localName\":\"Gold Cup Parade Day\",\"name\":\"Gold Cup Parade Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-PE\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-08-19\",\"localName\":\"Discovery Day\",\"name\":\"Discovery Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":false,\"counties\":[\"CA-YT\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-09-02\",\"localName\":\"Labour Day\",\"name\":\"Labour Day\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":true,\"counties\":null,\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-09-30\",\"localName\":\"National Day for Truth and Reconciliation\",\"name\":\"National Day for Truth and Reconciliation\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":true,\"counties\":null,\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-10-14\",\"localName\":\"Thanksgiving\",\"name\":\"Thanksgiving\",\"countryCode\":\"CA\",\"fixed\":false,\"global\":true,\"counties\":null,\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-11-11\",\"localName\":\"Armistice Day\",\"name\":\"Armistice Day\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":false,\"counties\":[\"CA-NL\"],\"launchYear\":null,\"types\":[\"Public\"]},{\"date\":\"2024-11-11\",\"localName\":\"Remembrance Day\",\"name\":\"Remembrance Day\",\"countryCode\":\"CA\",\"fixed\":true,\"global\":false,\"counties\":[\"CA-AB\",\"CA-BC\",\"CA-NB\",\"CA-NT\",\"CA-NS\",\"CA-NU\",\"CA-PE\",\"CA-SK\",\"CA-YT\"],\"launchYear\":null,\"types\":[\"Public\"]}]"
        val jsonStringHardCoded = holidayData.value
        // Convert the JSON array string to a JSONArray
        val jsonArray = JSONArray(jsonStringHardCoded)

        // List to hold the holidays
        val holidayList = mutableListOf<Holiday>()

        for(i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("localName")
            val date = jsonObject.getString("date")

            holidayList.add(Holiday(date, name))
        }
        allHolidays.value = holidayList
    }
    fun getHolidaysForDate(date: LocalDate){

        val matchingHolidays = allHolidays.value?.filter { LocalDate.parse(it.date) == date }
        if (!matchingHolidays.isNullOrEmpty()){
            dayHolidays.value = matchingHolidays
        }else{
            dayHolidays.value = emptyList()
        }
    }
}