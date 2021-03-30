package com.example.weatherly.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weatherly.model.Repository
import com.example.weatherly.model.data.WeatherResponse

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(getApplication())
    fun getDetailsWeather(lat : String, lon: String): LiveData<WeatherResponse> {
        return repository.getDetails(lat, lon)
    }
}