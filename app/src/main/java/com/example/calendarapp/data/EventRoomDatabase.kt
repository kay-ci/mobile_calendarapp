package com.example.calendarapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.calendarapp.domain.Event
import com.example.calendarapp.domain.Converters

@Database (entities = [(Event::class)], version = 2)//2 = with school themed entity
@TypeConverters(Converters::class)
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