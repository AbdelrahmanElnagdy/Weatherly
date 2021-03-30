package com.example.weatherly.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherly.R
import com.example.weatherly.model.data.WeatherResponse
import com.example.weatherly.view_model.FavouritesViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.fragment_favourites.*
import java.util.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherly.model.utilities.AppConstants
import com.example.weatherly.view.adapters.FavouriteRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.collections.ArrayList


class FavouritesFragment : Fragment() {
    private lateinit var viewModel: FavouritesViewModel
    var lat = 0.0
    var lon = 0.0
    lateinit var favAdapter: FavouriteRecyclerAdapter
    var favourites: ArrayList<WeatherResponse> = ArrayList()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)
            addBtn.setOnClickListener {
            placeAuto()
            }
        setAdapter()
        viewModel.getFavouriteData("0", "0").observe(viewLifecycleOwner,Observer{
            favAdapter.setList(it)
        })
        sharedPreferences= context?.getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)!!
        editor  = sharedPreferences?.edit()
    }
    private fun setAdapter(){
        recycler_fav.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        favAdapter = FavouriteRecyclerAdapter(favourites,this.requireContext(), lat.toString(), lon.toString(), viewModel, activity!!.supportFragmentManager)
        recycler_fav.adapter = favAdapter
    }

    fun placeAuto() {
        if (!Places.isInitialized()) {
            Places.initialize(this.requireContext(), "AIzaSyBL_za9z0eWrk_VFVN1TCuP32mQW19P52o")
        }
        val fields: List<Place.Field> = Arrays.asList<Place.Field>(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this.requireContext())
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var address : String?
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            var place : Place? = data?.let { Autocomplete.getPlaceFromIntent(it) }
            address = data?.let { Autocomplete.getPlaceFromIntent(it).name }
            lat = Autocomplete.getPlaceFromIntent(data!!).latLng?.latitude!!
            lon = Autocomplete.getPlaceFromIntent(data!!).latLng?.longitude!!
            val lonDecimal = BigDecimal(lon).setScale(4, RoundingMode.HALF_DOWN)
            val latDecimal = BigDecimal(lat).setScale(4, RoundingMode.HALF_DOWN)
            editor.putString(AppConstants.FAVOURITE_LAT, latDecimal.toString())
            editor.apply()
            editor.putString(AppConstants.FAVOURITE_LONG, lonDecimal.toString())
            editor.apply()

            viewModel.getFavouriteData(latDecimal.toString(), lonDecimal.toString())
            Log.i("TAG", "place:" + address)
            Log.i("TAG", "looooonggggg:" + (place?.latLng?.latitude ?: 0.0))

        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
           val status = data?.let { Autocomplete.getStatusFromIntent(it) }
            address = data?.let { Autocomplete.getPlaceFromIntent(it).name }
        }
    }

}