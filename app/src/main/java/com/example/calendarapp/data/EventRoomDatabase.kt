package com.example.calendarapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.calendarapp.domain.Event

@Database (entities = [(Event::class)], version = 1)
abstract class EventRoomDatabase : RoomDatabase(){
    abstract fun eventDao() : EventDao
    companion object{
        private var INSTANCE : EventRoomDatabase? = null

        fun getInstance(context: Context):EventRoomDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EventRoomDatabase::class.java,
                        "event_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance

            }
        }
    }


}