package com.example.weatherly.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherly.model.data.WeatherResponse

abstract class RoomDataBase {
  @Database(entities = arrayOf(WeatherResponse::class), version = 1, exportSchema = false)
  abstract class WeatherRoomDatabase : RoomDatabase() {

   abstract fun weatherDao(): Dao

   companion object {

    @Volatile
    private var INSTANCE: WeatherRoomDatabase? = null

    fun getDatabase(context: Context): WeatherRoomDatabase {

     return INSTANCE ?: synchronized(this) {
      val instance = Room.databaseBuilder(
       context.applicationContext,
       WeatherRoomDatabase::class.java,
       "weather_database"
      ).build()
      INSTANCE = instance
      instance
     }
    }
   }
  }
}