package com.example.weatherly.model.local

import android.app.Application
import androidx.room.Room

object AlertDataSource {
    fun getInstance(application: Application) : AlertDao {
        return Room.databaseBuilder(application, AlertDatabase::class.java, "alertDatabase").build().alertDao()
    }
}