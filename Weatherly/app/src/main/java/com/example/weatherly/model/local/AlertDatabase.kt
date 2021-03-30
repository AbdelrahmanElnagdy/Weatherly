package com.example.weatherly.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherly.model.data.AlertModel

@Database(entities = [AlertModel::class], version = 1, exportSchema = false)
abstract class AlertDatabase : RoomDatabase(){
    abstract fun alertDao(): AlertDao
}