package com.example.weatherly.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherly.model.data.WeatherResponse

@Dao
interface Dao {
    @Query("SELECT * FROM WeatherResponse WHERE lon = :lon AND lat = :lat")
    fun getData(lat: String, lon: String): LiveData<WeatherResponse>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(response: WeatherResponse?)
    @Query("SELECT * FROM WeatherResponse WHERE lon != :favLongitude AND lat != :favLatitude")
    fun getFavouriteData(favLatitude: String, favLongitude: String): LiveData<List<WeatherResponse>>
    @Query("DELETE FROM WeatherResponse WHERE lon = :long AND lat = :lat")
    fun deleteData(lat: String, long: String)
    @Query("SELECT * from WeatherResponse WHERE lon = :lon AND lat = :lat")
    fun getCurrentForBroadCast(lat:String,lon:String):WeatherResponse
}