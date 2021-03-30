package com.example.weatherly.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherly.R
import com.example.weatherly.model.data.DailyItem
import com.example.weatherly.model.data.HourlyItem
import com.example.weatherly.model.utilities.AppConstants
import com.example.weatherly.view.adapters.DailyRecyclerAdapter
import com.example.weatherly.view.adapters.HourlyRecyclerAdaprer
import com.example.weatherly.view_model.HomeViewModel
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var long = 0.0
    var lat = 0.0
    var hourly: ArrayList<HourlyItem> = ArrayList()
    var daily: ArrayList<DailyItem> = ArrayList()
    private lateinit var viewModel: HomeViewModel
    lateinit var hourlyAdapter : HourlyRecyclerAdaprer
    lateinit var daillyAdapter : DailyRecyclerAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    var myLocation: List<Address?> = ArrayList()
    var location = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getLastLocation()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        getLastLocation()
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.getDailyWeather().observe(viewLifecycleOwner, Observer {

                getLastLocation()
                setAdapters()
            if (it!=null) {
                daillyAdapter.daillyItem = it.daily as ArrayList<DailyItem>
                daillyAdapter.notifyDataSetChanged()
                hourlyAdapter.hourlyItem = it.hourly as ArrayList<HourlyItem>
                hourlyAdapter.notifyDataSetChanged()
                home_humidity.text = "Humidity: ${it.current?.humidity}"
                home_clouds.text = "Clouds: ${it.current?.clouds}"
                home_pressure.text = "Pressure: ${it.current?.pressure}"
                home_wind_speed.text = "Wind Speed: ${it.current?.windSpeed}"
                home_state.text = it.current?.weather?.first()?.description.toString()
                home_temp.text = it.current?.temp.toString().substringBefore(".")
                val calender = Calendar.getInstance()
                calender.timeInMillis = (it.current?.dt?.toLong() ?: 10) * 1000L
                val dateFormat = SimpleDateFormat("dd/MM/yyyy");
                home_date.text = dateFormat.format(calender.time)
                when(it.current?.weather?.first()?.icon){
                    "01d", "01n" -> icon.setImageResource(R.drawable.sunny)
                    "02d", "02n" -> icon.setImageResource(R.drawable.few_clouds)
                    "03d", "03n" -> icon.setImageResource(R.drawable.scattered_clouds)
                    "04d", "04n" -> icon.setImageResource(R.drawable.broken_clouds)
                    "09d", "09n" -> icon.setImageResource(R.drawable.shower_rain)
                    "10d", "10n" -> icon.setImageResource(R.drawable.rain)
                    "11d", "11n" -> icon.setImageResource(R.drawable.thunder_storm)
                    "13d", "13n" -> icon.setImageResource(R.drawable.snow)
                    "50d", "50n" -> icon.setImageResource(R.drawable.sunny)
                }
            }
        })
        editor= context?.getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE)!!.edit()

    }
    private fun setAdapters(){
        recycler_hours_home.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        hourlyAdapter = HourlyRecyclerAdaprer(hourly, this.requireContext(), location)
        recycler_hours_home.adapter = hourlyAdapter
        recycler_week_home.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        daillyAdapter = DailyRecyclerAdapter(daily, this.requireContext(), location)
        recycler_week_home.adapter = daillyAdapter
        home_location.text = location

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                Toast.makeText(this.requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 1000

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()

        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            lat = mLastLocation.latitude
            long = mLastLocation.longitude
            val lonDecimal = BigDecimal(long).setScale(4, RoundingMode.HALF_DOWN)
            val latDecimal = BigDecimal(lat).setScale(4, RoundingMode.HALF_DOWN)
            editor.putString(AppConstants.LANG_KEY, "$lonDecimal")
            editor.putString(AppConstants.LAT_KEY, "$latDecimal")
            editor.apply();
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            myLocation = geocoder.getFromLocation(lat, long, 1)
            location = myLocation[0]?.adminArea ?: (myLocation[0]!!.countryName)


        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager = this.requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this.requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()

            }
        }
    }


}