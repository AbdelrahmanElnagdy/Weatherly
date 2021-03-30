package com.example.weatherly.model

import android.R
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import com.example.weatherly.model.data.AlertModel
import com.example.weatherly.model.data.DailyItem
import com.example.weatherly.model.data.WeatherResponse
import com.example.weatherly.model.local.AlertDataSource
import com.example.weatherly.model.local.LocalDataSource
import com.example.weatherly.model.utilities.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class AlarmReceiver : BroadcastReceiver() {
    lateinit var mainContext: Context
    lateinit var repo:Repository
    lateinit var requiredWeatherResponse: WeatherResponse
    lateinit var requiredAlertResponse: AlertModel
    var alarmId by Delegates.notNull<Int>()
    var result = ""

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("rerceive", "didn't receive")
        mainContext = context
        var sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)
        alarmId = intent.getExtras()!!.getInt(AppConstants.ALARM_ID)
        repo = Repository(context.applicationContext as Application)
        repo.getCurrentForBroadCast()
        val lat = sharedPreferences.getString(AppConstants.LAT_KEY, "30.5547")
        val lon = sharedPreferences.getString(AppConstants.LANG_KEY,"32.2702")
        Log.i("lat", "$lat")
        Log.i("id", "$alarmId")
        runBlocking(Dispatchers.IO) {
            launch {
                requiredWeatherResponse = LocalDataSource.localDataSource(context.applicationContext as Application).getCurrentForBroadCast(lat!!,lon!!)
                requiredAlertResponse = AlertDataSource.getInstance(context.applicationContext as Application).getSingleAlarm(alarmId.toString())
            }
        }
        checkData(requiredAlertResponse)
    }

    private fun sendNotification() {
        var builder = Notification.Builder(mainContext)
        val manager = mainContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { val channel = NotificationChannel(AppConstants.NOTIFICATION_CHANNEL_ID, "Weatherly", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
            builder = Notification.Builder(mainContext, AppConstants.NOTIFICATION_CHANNEL_ID)
        } else {
            builder = Notification.Builder(mainContext)
        }
        builder.setContentTitle("Weatherly")
        builder.setContentText("Weather Report")
        builder.setSmallIcon(R.drawable.ic_menu_add)
        builder.setStyle(Notification.BigTextStyle().bigText(result))
        val notification: Notification = builder.build()
        manager.notify(10, notification)
    }


    private fun checkData(alertModel: AlertModel){
        for(day in alertModel.days!!){
            when(day){
                "saturday" -> {
                    checkWeatherDate(day,alertModel.alertType)}
                "sunday" -> {
                    checkWeatherDate(day,alertModel.alertType)}
                "monday" -> {
                    checkWeatherDate(day,alertModel.alertType)}
                "tuesday" -> {
                    checkWeatherDate(day,alertModel.alertType)}
                "wednesday" -> {
                    checkWeatherDate(day,alertModel.alertType)}
                "thursday" -> {
                    checkWeatherDate(day,alertModel.alertType)}
                "friday" -> {
                    checkWeatherDate(day,alertModel.alertType)}
            }
        }
        if(result != ""){
            sendNotification()
        }
    }



    private fun checkWeatherDate(reqDay:String,alertType:String){
        for(element in requiredWeatherResponse.daily!!){
            if(reqDay.toLowerCase() == getDayFromWeatherResponse(element!!.dt!!.toLong())){
                checkAlertTypeOfDate(requiredWeatherResponse.daily!!.indexOf(element),alertType,reqDay)
            }
        }
    }

    private fun getDayFromWeatherResponse(dt:Long):String{
        val calender = Calendar.getInstance()
        calender.timeInMillis = (dt)*1000L
        val dateFormat = SimpleDateFormat("EEEE");
        return dateFormat.format(calender.time).toLowerCase()
    }

    private fun checkAlertTypeOfDate(dailyNum:Int,alertType:String,reqDay: String){
        val item = requiredWeatherResponse.daily?.get(dailyNum)!!
        when(alertType){
            "Rain" -> { checkMaxMinRain(item,reqDay) }
            "Temperature" -> { checkMaxMinTemp(item,reqDay) }
            "Wind" -> { checkMaxMinWind(item,reqDay) }
            "Fog/Mist/Haze" -> { checkMaxMinHaze(item,reqDay) }
            "Snow" -> { checkMaxMinSnow(item,reqDay) }
            "Cloudiness" -> { checkMaxMinCloudness(item,reqDay) }
            "Thunderstorm" -> { checkMaxMinThunder(item,reqDay) }
        }

    }

    private fun checkMaxMinRain(item: DailyItem, reqDay: String){
        if(item.weather?.get(0)?.main == "Rain") {
            if (requiredAlertResponse.maxMinValue == "max") {
                if (requiredAlertResponse.thresholdValue <= item.rain!!) {
                    result += "\n${reqDay} Rain: more than ${item.rain}"
                }
            } else {
                if (requiredAlertResponse.thresholdValue > item.rain!!) {
                    result += "\n${reqDay} Rain: less than ${item.rain}"
                }
            }
        }
    }

    private fun checkMaxMinTemp(item: DailyItem, reqDay: String){
        if (requiredAlertResponse.maxMinValue == "max") {
            if (requiredAlertResponse.thresholdValue <= item.temp?.day!!) {
                result += "\n${reqDay} Temperature: more than ${item.temp.day}"
            }
        } else {
            if (requiredAlertResponse.thresholdValue > item.temp?.day!!) {
                result += "\n${reqDay} Temperature: less than ${item.temp.day}"
            }
        }

    }

    private fun checkMaxMinWind(item: DailyItem, reqDay: String){
        if (requiredAlertResponse.maxMinValue == "max") {
            if (requiredAlertResponse.thresholdValue <= item.windSpeed!!) {
                result += "\n${reqDay} Wind: more than ${item.windSpeed}"
            }
        } else {
            if (requiredAlertResponse.thresholdValue > item.windSpeed!!) {
                result += "\n${reqDay} Wind: less than ${item.windSpeed}"
            }
        }
    }

    private fun checkMaxMinHaze(item: DailyItem, reqDay: String){
        if(item.weather?.get(0)?.main == "Haze" ||item.weather?.get(0)?.main == "Mist" ||item.weather?.get(0)?.main == "Fog") {
            result += "\n${reqDay} This day has Haze"
        }
    }

    private fun checkMaxMinSnow(item: DailyItem, reqDay: String){
        if(item.weather?.get(0)?.main == "Snow") {
            result += "\n${reqDay} This day has Snow"
        }
    }

    private fun checkMaxMinCloudness(item: DailyItem, reqDay: String){
        if(item.weather?.get(0)?.main == "Clouds") {
            if (requiredAlertResponse.maxMinValue == "max") {
                if (requiredAlertResponse.thresholdValue <= item.clouds!!) {
                    result += "\n${reqDay} Cloudiness: more than ${item.clouds}"
                }
            } else {
                if (requiredAlertResponse.thresholdValue > item.temp?.day!!) {
                    result += "\n${reqDay} Cloudiness: more than ${item.clouds}"
                }
            }
        }
    }

    private fun checkMaxMinThunder(item: DailyItem, reqDay: String){
        if(item.weather?.get(0)?.main == "Thunderstorm") {
            result += "\n${reqDay} This day has ThunderStorm"
        }
    }





}