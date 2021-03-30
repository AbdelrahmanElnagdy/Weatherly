package com.example.weatherly.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.weatherly.model.Repository
import com.example.weatherly.model.data.AlertModel

class AddAlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(getApplication())

    fun insertAlert(alert: AlertModel){
        repository.insertAlert(alert)
    }
}