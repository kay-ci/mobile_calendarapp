package com.example.calendarapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.icu.util.Calendar
import android.icu.util.ULocale
import android.telephony.TelephonyManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.calendarapp.domain.ForecastData
import com.example.calendarapp.domain.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

class CalendarViewModel (application: Application) : ViewModel() {
    var selectedDate by mutableStateOf("")
    val fetchedYear = mutableIntStateOf(0)
    private val appContext = application // Holds application context
    var holidayData = mutableStateOf("") // Will hold data fetched from file
    private val filename = "holidayData"
    private val calendar: Calendar = Calendar.getInstance(ULocale("en_US@calendar=gregorian"))
    private val _currentDay = mutableIntStateOf(0)
    val currentDay: MutableState<Int> = _currentDay
    private val _currentMonth = mutableStateOf("")
    val currentMonth: MutableState<String> = _currentMonth
    private val _currentYear = mutableIntStateOf(0)
    val currentYear: MutableState<Int> = _currentYear
    private val _daysInMonth = mutableStateOf<List<String>>(emptyList())
    val daysInMonth : MutableState<List<String>> = _daysInMonth
    private val _firstWeekDay = mutableIntStateOf(Calendar.SUNDAY)
    val firstWeekDay : MutableState<Int> = _firstWeekDay
    val allEvents : LiveData<List<Event>>
    private val repository : EventRepository
    val searchResults: MutableLiveData<List<Event>>
    val allHolidays: MutableLiveData<List<Holiday>> = MutableLiveData()
    val dayHolidays: MutableLiveData<List<Holiday>> = MutableLiveData()
    private val weatherRepository = WeatherRepository()
    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData> get() = _weatherData
    private val _weatherDataForecast = MutableLiveData<ForecastData>()
    val weatherDataForecast: LiveData<ForecastData> get() = _weatherDataForecast

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
    fun setDate(newDate: String){
        selectedDate = newDate
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

    fun fetchNextWeatherData(latitude: String, longitude: String) {
        weatherRepository.getFiveDaysWeather(latitude, longitude)
            .enqueue(object : Callback<ForecastData> {
                override fun onResponse(call: Call<ForecastData>, response: Response<ForecastData>) {
                    if (response.isSuccessful) {
                        viewModelScope.launch(Dispatchers.Main) {
                            _weatherDataForecast.value = response.body()!!
                        }
                    } else {
                        // Handle error
                        Log.e("Weather", "Error fetching weather data: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<ForecastData>, t: Throwable) {
                    // Handle failure
                    Log.e("Weather", "Failure while fetching weather data: ${t.message}")
                }
            })
    }

    fun getNextFiveDays(): List<String> {
        val currentDayOfWeek = LocalDate.now().dayOfWeek
        val nextFiveDays = mutableListOf<String>()

        for (i in 1..5) {
            val nextDay = currentDayOfWeek.plus(i.toLong())
            val dayName = nextDay.getDisplayName(TextStyle.FULL, Locale.getDefault())
            nextFiveDays.add(dayName)
        }

        return nextFiveDays
    }

    fun getDayOfWeek(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date: Date = dateFormat.parse(dateString) ?: Date()

        // Format the date to get the day of the week
        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dayOfWeekFormat.format(date)
    }

    fun fetchHolidayData(){
        val telephonyManager = appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?

        val countryCode = telephonyManager?.simCountryIso
        //Create a coroutine to fetch the data
        viewModelScope.launch ( Dispatchers.IO ){
            val urlString = "https://date.nager.at/api/v3/PublicHolidays/${currentYear.value}/$countryCode"
            fetchedYear.value = currentYear.value
            DownloadData(context = appContext).fetchData(filename, urlString)
        }
    }

    fun getHolidaysFromFile() {
        /*Fetch the data asynchronously via a coroutine. It needs to run on the
        Main thread, since the data is required by the Main thread.
         */
        viewModelScope.launch(Dispatchers.Main) {
            val fileData = TempStorage(appContext).readDataFromFile(filename)
            holidayData.value = fileData
            println("Raw JSON String Data: $fileData")
            getAllHolidays()
        }
    }
    private fun getAllHolidays() {
        // Uses json fetched from file to put all objects into a list

        val jsonString = holidayData.value
        // Convert the JSON array string to a JSONArray
        val jsonArray = JSONArray(jsonString)

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
        // Get all holidays for a specific date

        val matchingHolidays = allHolidays.value?.filter { LocalDate.parse(it.date) == date }
        if (!matchingHolidays.isNullOrEmpty()){
            dayHolidays.value = matchingHolidays
        }else{
            dayHolidays.value = emptyList()
        }
    }
}