package com.example.weatherly.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherly.R
import com.example.weatherly.model.data.DailyItem
import com.example.weatherly.model.data.HourlyItem
import com.example.weatherly.model.utilities.AppConstants
import com.example.weatherly.view.adapters.DailyRecyclerAdapter
import com.example.weatherly.view.adapters.HourlyRecyclerAdaprer
import com.example.weatherly.view_model.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import com.example.weatherly.view.adapters.DetailsDailyAdapter
import com.example.weatherly.view.adapters.DetailsHourlyAdapter
import com.example.weatherly.view_model.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {
    var long = "0.0"
    var lat = "0.0"
    var hourly: ArrayList<HourlyItem> = ArrayList()
    var daily: ArrayList<DailyItem> = ArrayList()
    private lateinit var viewModel: DetailsViewModel
    lateinit var hourlyAdapter : DetailsHourlyAdapter
    lateinit var daillyAdapter : DetailsDailyAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var myLocation: List<Address?> = ArrayList()
    var location = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lat = arguments?.getString("lat").toString()
        long = arguments?.getString("lon").toString()
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        viewModel.getDetailsWeather(lat, long).observe(viewLifecycleOwner, Observer {
            setAdapters()
            if (it!=null) {
                daillyAdapter.daillyItem = it.daily as ArrayList<DailyItem>
                daillyAdapter.notifyDataSetChanged()
                hourlyAdapter.hourlyItem = it.hourly as ArrayList<HourlyItem>
                hourlyAdapter.notifyDataSetChanged()
                details_humidity.text = "Humidity: ${it.current?.humidity}"
                details_clouds.text = "Clouds: ${it.current?.clouds}"
                details_pressure.text = "Pressure: ${it.current?.pressure}"
                details_wind_speed.text = "Wind Speed: ${it.current?.windSpeed}"
                details_state.text = it.current?.weather?.first()?.description.toString()
                details_temp.text = it.current?.temp.toString().substringBefore(".")
                val calender = Calendar.getInstance()
                calender.timeInMillis = (it.current?.dt?.toLong() ?: 10) * 1000L
                val dateFormat = SimpleDateFormat("dd/MM/yyyy");
                details_date.text = dateFormat.format(calender.time)
                when(it.current?.weather?.first()?.icon){
                    "01d", "01n" -> dicon.setImageResource(R.drawable.sunny)
                    "02d", "02n" -> dicon.setImageResource(R.drawable.few_clouds)
                    "03d", "03n" -> dicon.setImageResource(R.drawable.scattered_clouds)
                    "04d", "04n" -> dicon.setImageResource(R.drawable.broken_clouds)
                    "09d", "09n" -> dicon.setImageResource(R.drawable.shower_rain)
                    "10d", "10n" -> dicon.setImageResource(R.drawable.rain)
                    "11d", "11n" -> dicon.setImageResource(R.drawable.thunder_storm)
                    "13d", "13n" -> dicon.setImageResource(R.drawable.snow)
                    "50d", "50n" -> dicon.setImageResource(R.drawable.sunny)
                }

            }
        })
        sharedPreferences= context?.getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)!!
        editor  = sharedPreferences.edit()
    }
    private fun setAdapters(){
        recycler_hours_details.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        hourlyAdapter = DetailsHourlyAdapter(hourly, this.requireContext(), location,  lat, long)
        recycler_hours_details.adapter = hourlyAdapter
        recycler_week_details.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        daillyAdapter = DetailsDailyAdapter(daily, this.requireContext(), location, lat, long)
        recycler_week_details.adapter = daillyAdapter
        details_location.text = getAddress(lat.toDouble(), long.toDouble())

    }
    private fun getAddress(lat: Double, lon: Double): String{
        val geocoder = Geocoder(context, Locale.getDefault())
        var myLocation = geocoder.getFromLocation(lat, lon, 1)
        location = myLocation[0]?.adminArea.toString()
        return location
    }

}