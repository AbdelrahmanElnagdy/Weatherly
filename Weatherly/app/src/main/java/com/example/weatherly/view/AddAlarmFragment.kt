package com.example.weatherly.view

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.weatherly.R
import com.example.weatherly.model.data.AlertModel
import com.example.weatherly.model.utilities.AppConstants
import com.example.weatherly.view_model.AddAlarmViewModel
import kotlinx.android.synthetic.main.fragment_add_alarm.*
import kotlinx.android.synthetic.main.fragment_settings.*
import org.angmarch.views.OnSpinnerItemSelectedListener
import java.text.SimpleDateFormat
import java.util.*


class AddAlarmFragment : Fragment() {    private lateinit var viewModel: AddAlarmViewModel
    private var lang = "en"
    private var alarmType = ""
    private var calenderFrom = Calendar.getInstance()
    private var calenderTo = Calendar.getInstance()
    private var timeCondition = "anytime"
    private var maxOrMin = "min"
    private var thresholdValue = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_alarm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lang = (activity?.getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)?.getString(
            AppConstants.LANGUAGE_KEY,"en").toString())
        day_picker.locale = Locale.forLanguageTag(lang)
        viewModel = ViewModelProvider(this).get(AddAlarmViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addSpinner()
        R.color.dayPressed
        saveBtn.setOnClickListener {
            if(isDataValidated()){
                Toast.makeText(requireContext(),"Data is Validated", Toast.LENGTH_SHORT).show()  //add object to database
                thresholdValue = valueET.text.trim().toString().toDouble()
                val alert = AlertModel(days = returnChoosenDays(),alertType = alarmType,maxMinValue = maxOrMin,thresholdValue = thresholdValue)
                viewModel.insertAlert(alert)
                activity?.onBackPressed()

            }
        }


       radioGroupMaxMin.setOnCheckedChangeListener { radioGroup, i ->
            if(i==R.id.maxRB){
                maxOrMin = "max"
                Toast.makeText(requireContext(),"max", Toast.LENGTH_SHORT).show()
            }
            else{ maxOrMin = "min"
                Toast.makeText(requireContext(),"min", Toast.LENGTH_SHORT).show()
            }
        }




    }

    private fun isDataValidated(): Boolean {
        if(returnChoosenDays() == null){
            Toast.makeText(requireContext(),resources.getString(R.string.weekend), Toast.LENGTH_SHORT).show()
            return false
        }else if(alarmType == ""){
            Toast.makeText(requireContext(),resources.getString(R.string.alertType), Toast.LENGTH_SHORT).show()
            return false
        }else if(valueET.text.trim().isEmpty()){
            Toast.makeText(requireContext(),resources.getString(R.string.threshold), Toast.LENGTH_SHORT).show()
            return false
        }else{
            return true
        }
    }

    private fun returnChoosenDays():List<String>?{
        day_picker.locale = Locale.forLanguageTag(lang)
        var selectedDays = day_picker.selectedDays
        day_picker.setDaySelectionChangedListener { it ->
            selectedDays = day_picker.selectedDays
        }
        var arr = mutableListOf<String>()
        selectedDays.forEach { arr.add(it.name.toLowerCase()) }
        return if(arr.isEmpty()) null else arr
    }
    private fun addSpinner(){
        ArrayAdapter.createFromResource(requireContext(), R.array.alertTypes, R.layout.spinner_item).also {
                adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            nice_spinner.adapter = adapter
//            val units = arrayOf("metric","imperial",  "standard")
            val dataset = resources.getStringArray(R.array.alertTypes).toList()
            nice_spinner.adapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, dataset)
            nice_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val ara = mapOf("" to "","امطار" to "Rain","درجة حرارة" to "Temperature","رياح" to "Wind","ضباب/شبورة" to "Fog/Mist/Haze","ثلوج" to "Snow","غيوم" to "Cloudiness","عواصف رعدية" to "Thunderstorm")
                    alarmType = parent?.getItemAtPosition(position).toString()
                    if(lang=="ar"){
                        alarmType = ara[alarmType].toString()
                    }
                    if(alarmType == "Rain" || alarmType == "Temperature" || alarmType == "Wind" || alarmType == "Cloudiness"){
                        valueET.visibility = View.VISIBLE
                        valueET.setText("")
                    }else{
                        valueET.visibility = View.GONE
                        valueET.setText("0.0")
                    }

                }
            }
        }

    }


}