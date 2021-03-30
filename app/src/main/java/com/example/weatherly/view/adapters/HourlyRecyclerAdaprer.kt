package com.example.weatherly.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherly.R
import com.example.weatherly.model.data.HourlyItem
import kotlinx.android.synthetic.main.item_hourly.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HourlyRecyclerAdaprer(hourlyItem: ArrayList<HourlyItem>, context: Context, currentLocation: String): RecyclerView.Adapter<HourlyRecyclerAdaprer.ViewHolder>() {
    var hourlyItem = hourlyItem
    var context = context
    var location = currentLocation

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hourly, parent, false))
    }
    override fun getItemCount() = hourlyItem.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.clouds.text = "Clouds: ${ hourlyItem[position].clouds.toString() }"
        holder.windSpeed.text = "Wind Speed: ${ hourlyItem[position].windSpeed.toString() }"
        holder.temp.text = hourlyItem[position].temp.toString().substringBefore(".")
        holder.pressure.text = "Pressure: ${ hourlyItem[position].pressure.toString() }"
        holder.humidity.text = "Humidity: ${ hourlyItem[position].humidity.toString() }"
        holder.state.text = hourlyItem[position].weather?.first()?.description.toString()
        val calender = Calendar.getInstance()
        calender.timeInMillis = (hourlyItem[position].dt?.toLong() ?: 10)*1000L
        val dateFormat = SimpleDateFormat("HH:mm");
        holder.date.text = dateFormat.format(calender.time)
        holder.location.text = location
        when(hourlyItem[position].weather?.first()?.icon){
            "01d", "01n" -> holder.icon.setImageResource(R.drawable.sunny)
            "02d", "02n" -> holder.icon.setImageResource(R.drawable.few_clouds)
            "03d", "03n" -> holder.icon.setImageResource(R.drawable.scattered_clouds)
            "04d", "04n" -> holder.icon.setImageResource(R.drawable.broken_clouds)
            "09d", "09n" -> holder.icon.setImageResource(R.drawable.shower_rain)
            "10d", "10n" -> holder.icon.setImageResource(R.drawable.rain)
            "11d", "11n" -> holder.icon.setImageResource(R.drawable.thunder_storm)
            "13d", "13n" -> holder.icon.setImageResource(R.drawable.snow)
            "50d", "50n" -> holder.icon.setImageResource(R.drawable.sunny)
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val temp: TextView = view.hourly_temp
        val pressure: TextView = view.hourly_pressure
        val clouds: TextView = view.hourly_clouds
        val humidity: TextView = view.hourly_humidity
        val windSpeed: TextView = view.hourly_wind_speed
        val location: TextView = view.hourly_location
        val date: TextView = view.hourly_date
        val state: TextView = view.hourly_state
        val icon = view.icon
    }




}