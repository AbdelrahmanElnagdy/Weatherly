package com.example.weatherly.model.local

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherly.model.data.WeatherResponse
import com.example.weatherly.model.local.Dao

object LocalDataSource {
    fun localDataSource(application: Application): Dao {
        return RoomDataBase.WeatherRoomDatabase.getDatabase(application).weatherDao()
    }

}