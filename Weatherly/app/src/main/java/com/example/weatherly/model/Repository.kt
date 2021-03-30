package com.example.weatherly.model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.location.Address
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherly.model.data.AlertModel
import com.example.weatherly.model.data.WeatherResponse
import com.example.weatherly.model.local.AlertDataSource
import com.example.weatherly.model.local.LocalDataSource
import com.example.weatherly.model.remote.WeatherService
import com.example.weatherly.model.utilities.AppConstants
import kotlinx.coroutines.*


class Repository(application: Application) {

    var remoteDataSource= WeatherService
    var localDataSource =  LocalDataSource.localDataSource(application)
    var sharedPreferences: SharedPreferences = application.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE)
    private val localAlarmDatabase = AlertDataSource.getInstance(application)
    var sharedPref2 = application.getSharedPreferences(AppConstants.SETTINGS_SHARED_PREF, MODE_PRIVATE)
    val lang = sharedPreferences.getString(AppConstants.LANG_KEY, "0")
    val lat = sharedPreferences.getString(AppConstants.LAT_KEY, "0")
    var language = sharedPref2.getString(AppConstants.LANGUAGE_KEY, "ar")
    var tempUnits = sharedPref2.getString(AppConstants.UNIT_KEY, "metric")


     fun loadHomeData(): LiveData<WeatherResponse> {

         CoroutineScope(Dispatchers.IO).launch {
                 try {
                     val response = remoteDataSource.getWeatherService().getWeather(lat!!, lang!!,AppConstants.EXCLUDE_MINUTELY, tempUnits!!,language!!, AppConstants.API_KEY)
                     if (response.isSuccessful){
                         response.body()?.let { localDataSource.insert(it) }

                     }
                 } catch (e : Exception){
                     Log.i("response error", e.message.toString())
                 }
             }
         return localDataSource.getData(lat!!, lang!!)
}
     fun loadFavouriteData(latitude: String, longitude: String): LiveData<List<WeatherResponse>>{
        val exceptionHandlerException = CoroutineExceptionHandler { _, throwable ->
        }
        CoroutineScope(Dispatchers.IO+ exceptionHandlerException).launch {
            if( latitude!= "0"&& longitude != "0"){
                val response = remoteDataSource.getWeatherService().getWeather(latitude,
                    longitude,AppConstants.EXCLUDE_MINUTELY, tempUnits!!,language!!, AppConstants.API_KEY)
                if (response.isSuccessful){
                    localDataSource.insert(response.body()!!)
                    Log.i("anyyyyy", response.body().toString())
                }
            }
        }

return localDataSource.getFavouriteData(lat!!, lang!!)
    }

    fun deleteFavouriteData(lat: String, long: String){
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.deleteData(lat, long)
        }
    }
    fun getDetails(latitude: String, longitude: String):LiveData<WeatherResponse>{
        return localDataSource.getData(latitude, longitude)
    }
    fun getCurrentForBroadCast(){
        runBlocking(Dispatchers.IO) {
            launch {
                try{
                    if (lat != null&& lang!= null) {
                        val response = remoteDataSource.getWeatherService().getWeather(lat, lang,AppConstants.EXCLUDE_MINUTELY, tempUnits.toString(),language.toString(), AppConstants.API_KEY)
                        if (response.isSuccessful) {
                            localDataSource.insert(response.body())
                            localDataSource.deleteData(lat,lang)
                        }
                    }
                }catch(e:Exception){
                    Log.i("error", "${e.message.toString()}")
                }
            }
        }
    }

    fun deleteAlarmById(id:String) {
        CoroutineScope(Dispatchers.IO).launch {
            localAlarmDatabase.deleteAlarmById(id)
        }
    }
    fun insertAlert(alert: AlertModel) {
        CoroutineScope(Dispatchers.IO).launch {
            localAlarmDatabase.insertAlarm(alert)
        }
    }

    fun getAllAlerts(): LiveData<List<AlertModel>> {
        return localAlarmDatabase.getAllAlarms()
    }


}


