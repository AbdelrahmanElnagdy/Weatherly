package com.example.weatherly.view

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.weatherly.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var fragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        bottomMenu()
    }



    private fun bottomMenu(){
        val id : Int
        bottom_nav_menu.setItemSelected(R.id.botton_nav_home, true)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_containter, HomeFragment()).commit()
        bottom_nav_menu.setOnItemSelectedListener { _ ->
            when (bottom_nav_menu.getSelectedItemId()) {
                R.id.botton_nav_setting -> {
                    fragment = SettingsFragment()
                }
                R.id.botton_nav_weather -> {
                    fragment = WeatherFragment()
                }
                R.id.botton_nav_home -> {
                    fragment = HomeFragment()
                }
                R.id.botton_nav_favorite -> {
                    fragment = FavouritesFragment()
                }
            }
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out).replace(R.id.fragment_containter, fragment)
                    .addToBackStack(null)
                    .commit()

        }

    }
}