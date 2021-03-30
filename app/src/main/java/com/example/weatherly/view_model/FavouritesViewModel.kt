package com.example.weatherly.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weatherly.model.Repository
import com.example.weatherly.model.data.WeatherResponse

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(getApplication())
    fun getFavouriteData(lat: String, long: String): LiveData<List<WeatherResponse>>{
        return repository.loadFavouriteData(lat, long)
    }
    fun deleteFavouriteData(lat: String, long: String){
        repository.deleteFavouriteData(lat, long)
    }
}