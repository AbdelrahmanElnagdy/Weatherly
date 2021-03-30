package com.example.weatherly.view.adapters

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherly.R
import com.example.weatherly.model.data.DailyItem
import kotlinx.android.synthetic.main.item_daily.view.*
import java.text.SimpleDateFormat
import java.util.*

class DetailsDailyAdapter (dailyItem: ArrayList<DailyItem>, context: Context, currentLocation: String,var lat: String,var lon: String): RecyclerView.Adapter<DetailsDailyAdapter.ViewHolder>()  {
        var daillyItem = dailyItem
        var context = context


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_daily, parent, false))
        }
        override fun getItemCount() = daillyItem.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.location.text = getAddress(lat.toDouble(), lon.toDouble())
            holder.clouds.text = "Clouds: ${ daillyItem[position].clouds.toString() }"
            holder.windSpeed.text = "Wind Speed: ${ daillyItem[position].windSpeed.toString() }"
            holder.temp.text = daillyItem[position].temp?.day.toString().substringBefore(".")
            holder.pressure.text = "Prssure: ${ daillyItem[position].pressure.toString() }"
            holder.humidity.text = "Humidity: ${daillyItem[position].humidity.toString()}"
            holder.state.text = daillyItem[position].weather?.first()?.description.toString()
            val calender = Calendar.getInstance()
            calender.timeInMillis = (daillyItem[position].dt?.toLong() ?: 10)*1000L
            val dateFormat = SimpleDateFormat("dd/MM/yyyy");
            holder.date.text = dateFormat.format(calender.time)
            when(daillyItem[position].weather?.first()?.icon){
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
    private fun getAddress(lat: Double, lon: Double): String{
        val geocoder = Geocoder(context, Locale.getDefault())
        var myLocation = geocoder.getFromLocation(lat, lon, 1)
        var location = myLocation[0]?.adminArea.toString()
        return location
    }

        class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
            val temp: TextView = view.daily_temp
            val pressure: TextView = view.daily_pressure
            val clouds: TextView = view.daily_clouds
            val humidity: TextView = view.daily_humidity
            val windSpeed: TextView = view.daily_wind_speed
            val location: TextView = view.daily_location
            val date: TextView = view.daily_date
            val state: TextView = view.daily_state
            val icon = view.icon
        }
    }
