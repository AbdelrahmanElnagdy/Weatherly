package com.example.weatherly.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weatherly.model.Repository
import com.example.weatherly.model.data.AlertModel

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(getApplication())

    fun getAllData(): LiveData<List<AlertModel>> {
        return repository.getAllAlerts()
    }

    fun deleteAlarmById(id:String){
        return repository.deleteAlarmById(id)
    }
}