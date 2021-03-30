package com.example.weatherly.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weatherly.model.Repository
import com.example.weatherly.model.data.WeatherResponse

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(getApplication())
    fun getDailyWeather(): LiveData<WeatherResponse>{
        return repository.loadHomeData()
    }

}