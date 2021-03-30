package com.example.weatherly.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherly.model.data.AlertModel

@Dao
interface AlertDao {
    @Insert
    suspend fun insertAlarm(alert : AlertModel)

    @Query("SELECT * FROM AlertModel ")
    fun getAllAlarms(): LiveData<List<AlertModel>>

    @Query("SELECT * FROM AlertModel where id = :alertId")
    fun getSingleAlarm(alertId:String):AlertModel

    @Query("DELETE FROM AlertModel where id = :alertId")
    fun deleteAlarmById(alertId:String)
}