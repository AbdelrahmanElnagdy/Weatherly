package com.example.weatherly.view.adapters

import android.app.AlertDialog
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherly.R
import com.example.weatherly.model.data.WeatherResponse
import com.example.weatherly.view.DetailsFragment
import com.example.weatherly.view_model.FavouritesViewModel
import kotlinx.android.synthetic.main.item_favourite.view.*
import java.util.*


class FavouriteRecyclerAdapter(favouritesItem: ArrayList<WeatherResponse>, context: Context, lat: String,lon: String, viewModel: FavouritesViewModel, val fragmentManager : FragmentManager): RecyclerView.Adapter<FavouriteRecyclerAdapter.ViewHolder>(), View.OnLongClickListener  {
    var favItem = favouritesItem
    var context = context
    val favViewModel = viewModel
    val lat = lat
    val long = lon
    var location = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return  ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_favourite, parent, false))
    }

    override fun getItemCount() = favItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.temp.text = favItem[position].current?.temp?.toString()?.substringBefore(".")
        holder.location.text = getAddress(favItem[position].lat , favItem[position].lon)
        holder.root.setOnLongClickListener {
            showDialog(favItem[position].lat.toString(), favItem[position].lon.toString())
            return@setOnLongClickListener true
        }
        holder.root.setOnClickListener {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putString("lat",favItem[position].lat.toString() )
            args.putString("lon", favItem[position].lon.toString())
            fragment.setArguments(args)
            fragmentManager.beginTransaction().replace(R.id.fragment_containter,fragment).addToBackStack(null).commit()
        }

    }
    private fun getAddress(lat: Double, lon: Double): String{
        val geocoder = Geocoder(context, Locale.getDefault())
        var myLocation = geocoder.getFromLocation(lat, lon, 1)
//        location = myLocation[0].adminArea ?: myLocation[0].countryName?: "no name found"
        return location
    }

    private fun showDialog(lat: String, lon: String){
        val builder = AlertDialog.Builder(context).setCancelable(false)
        builder.setTitle("Warning")
        builder.setMessage("Are you sure you want to delete this location?")
        builder.setPositiveButton("Yes"){ _, _ ->
            favViewModel.deleteFavouriteData(lat, lon)
        }
        builder.setNegativeButton("No"){_, _ -> }
        builder.show()
    }

     fun setList(list: List<WeatherResponse>){
        favItem.clear()
        favItem.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val temp = view.fav_temp
        val location = view.fav_location
        val root = view.favourite_item
    }

    override fun onLongClick(v: View?): Boolean {
        showDialog(lat, long)
        return true
    }


}