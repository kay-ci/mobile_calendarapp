package com.example.calendarapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.calendarapp.domain.Event

@Database (entities = [(Event::class)], version = 1)
abstract class EventRoomDatabase : RoomDatabase(){

}