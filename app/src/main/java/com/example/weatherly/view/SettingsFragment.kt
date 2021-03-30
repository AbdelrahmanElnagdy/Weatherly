package com.example.weatherly.view

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.weatherly.R
import com.example.weatherly.model.Repository
import com.example.weatherly.model.utilities.AppConstants
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*


class SettingsFragment : Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor :SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     sharedPreferences  = activity?.getSharedPreferences(
         AppConstants.SETTINGS_SHARED_PREF,
         AppCompatActivity.MODE_PRIVATE
     )!!
       editor=  sharedPreferences?.edit()
        addUnitsSpinner()
        addLanguageSpinner()
    }

    private fun addUnitsSpinner(){
        ArrayAdapter.createFromResource(requireContext(), R.array.units_array, R.layout.spinner_item).also {
                adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            units_spinner.adapter = adapter
            val units = arrayOf("metric","imperial",  "standard")
            units_spinner.adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, units)
            units_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Textunits.text = "Temperature units: ${sharedPreferences.getString(AppConstants.UNIT_KEY, "metric")}"
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    editor.putString(AppConstants.UNIT_KEY, units[position])?.apply()
                    Textunits.text = "Temperature units: ${sharedPreferences.getString(AppConstants.UNIT_KEY, "metric")}"
                }
            }
        }
    }

    private fun addLanguageSpinner(){
        ArrayAdapter.createFromResource(requireContext(), R.array.language_array, R.layout.spinner_item).also {
                adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            language_spinner.adapter = adapter
            val languages = arrayOf("English", "Arabic")
            language_spinner.adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, languages)
            language_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    text_language.text = "Language: ${languages[position]}"

                    if (languages[position] == "English"){
                        editor.putString(AppConstants.LANGUAGE_KEY, "en")?.commit()
                        setLocale(requireActivity(), "en")
                    }else if(languages[position] == "Arabic"){
                        editor.putString(AppConstants.LANGUAGE_KEY, "ar")?.commit()
                        setLocale(requireActivity(), "ar")
                    }
                    val language = sharedPreferences?.getString(AppConstants.LANGUAGE_KEY, "fr")
                    Log.i("language", "$language")

                }

            }
        }
    }
    fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

}