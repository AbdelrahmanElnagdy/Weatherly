package com.example.weatherly.model.remote

import com.example.weatherly.model.data.WeatherResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

 object WeatherService {


    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    fun getWeatherService(): WeatherAPI {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(WeatherAPI::class.java)
    }


 }