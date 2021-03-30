package com.example.weatherly.model.remote

import com.example.weatherly.model.data.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("onecall")
    suspend fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("exclude") exclude:String, @Query("units") units: String, @Query("lang") language:String, @Query("appid") appId: String): Response<WeatherResponse>
}
