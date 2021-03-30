package com.example.weatherly.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherly.R
import com.example.weatherly.model.AlarmReceiver
import com.example.weatherly.model.data.AlertModel
import com.example.weatherly.model.utilities.AppConstants
import com.example.weatherly.view.adapters.AlarmRecyclerAdapter
import com.example.weatherly.view_model.AlarmViewModel
import kotlinx.android.synthetic.main.fragment_weather.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherFragment : Fragment() {

    private lateinit var viewModel: AlarmViewModel
    private var calenderEvent = Calendar.getInstance()
    private var hourDuration = 24
    private var alarmList = ArrayList<AlertModel>()
    private lateinit var sharedPref: SharedPreferences
    private var switchCase = false
    private lateinit var alarmAdaptor: AlarmRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_weather, container, false)

    }

    private fun calenderTime(textView: TextView, hour:Int, min:Int){
        TimePickerDialog(requireContext(), object: TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                calenderEvent = Calendar.getInstance()
                calenderEvent.set(Calendar.HOUR_OF_DAY,p1)
                calenderEvent.set(Calendar.MINUTE,p2)
                calenderEvent.set(Calendar.SECOND,0)
                textView.setText(SimpleDateFormat("HH:mm").format(calenderEvent.time))
                Log.i("calender","${calenderEvent.timeInMillis}")

            }
        }, hour, min, false).show()
    }

//    private fun registerAll(){
//        val notifyIntent = Intent(context, AlarmReceiver::class.java)
//        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        for(element in alarmList){
//            if(Calendar.getInstance().timeInMillis >= calenderEvent.timeInMillis){
//                notifyIntent.putExtra(AppConstants.ALARM_ID,element.id)
//                var time = calenderEvent.timeInMillis
//                calenderEvent.timeInMillis = time.plus(AppConstants.HOUR_24_IN_SECONDS)
//                var pendingIntent = PendingIntent.getBroadcast(context,element.id,notifyIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT)
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis,pendingIntent)
//                if(hourDuration == 72){
//                    pendingIntent = PendingIntent.getBroadcast(context,element.id+1000,notifyIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT)
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS,pendingIntent)
//                    pendingIntent = PendingIntent.getBroadcast(context,element.id+2000,notifyIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT)
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_48_IN_SECONDS,pendingIntent)
//                }else if(hourDuration == 48){
//                    pendingIntent = PendingIntent.getBroadcast(context,element.id+1000,notifyIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT)
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS,pendingIntent)
//                }
//            }else{
//                notifyIntent.putExtra(AppConstants.ALARM_ID,element.id)
//                var pendingIntent = PendingIntent.getBroadcast(activity,element.id,notifyIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT)
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis,pendingIntent)
//                if(hourDuration == 72){
//                    pendingIntent = PendingIntent.getBroadcast(context,element.id+1000,notifyIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT)
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS,pendingIntent)
//                    pendingIntent = PendingIntent.getBroadcast(context,element.id+2000,notifyIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT)
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_48_IN_SECONDS,pendingIntent)
//                }else if(hourDuration == 48){
//                    pendingIntent = PendingIntent.getBroadcast(context,element.id+1000,notifyIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT)
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS,pendingIntent)
//                }
//            }
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        alarmAdaptor = AlarmRecyclerAdapter(ArrayList(),viewModel)
        initRecyclers()
        sharedPref = activity?.getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE)!!
        switchCase = sharedPref!!.getBoolean(AppConstants.isSwitchOn,false)
        viewModel.getAllData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it!= null){
                alarmList.addAll(it)
                alarmAdaptor.setIncomingList(it)
            }
            addBtn.setOnClickListener(View.OnClickListener {
                val fragment = AddAlarmFragment()
                val ft = getFragmentManager()!!.beginTransaction().replace(R.id.fragment_containter,fragment).addToBackStack(null).commit()
            })

        })

        onOffSwitch.setOnClickListener{
            if(alarmTimeTV.text == "--:--"){
                Toast.makeText(requireContext(),"Please choose time to make alarm at",Toast.LENGTH_SHORT).show()
                onOffSwitch.isChecked=false
            }else if(alarmList== null || alarmList.size ==0){
                Toast.makeText(requireContext(),"kindly add alert",Toast.LENGTH_SHORT).show()
                onOffSwitch.isChecked=false
            }else if(onOffSwitch.isChecked){
                Toast.makeText(requireContext(),"Alarm is On",Toast.LENGTH_SHORT).show()
                alarmStateTV.text = "Alarm is On"
                registerAll()
                cardView2.visibility = View.GONE
                groupRB.visibility = View.GONE
                sharedPref.edit()?.putBoolean(AppConstants.isSwitchOn,true)?.apply()
            }else{
                Toast.makeText(requireContext(),"Alarm is Off",Toast.LENGTH_SHORT).show()
                alarmStateTV.text = "Alarm is Off"
                cardView2.visibility = View.VISIBLE
                groupRB.visibility = View.VISIBLE
                alarmTimeTV.text = "--:--"
                unregisterAll()
                sharedPref.edit()?.putBoolean(AppConstants.isSwitchOn,false)?.apply()
            }
        }

        alarmTimeTV.setOnClickListener {
            calenderTime(it as TextView,calenderEvent.time.hours,calenderEvent.time.minutes)
        }

        groupRB.setOnCheckedChangeListener { radioGroup, i ->
            if(i == R.id.radioButton24){
                hourDuration = 24
                Toast.makeText(requireContext(),"alarm is running for $hourDuration hours",Toast.LENGTH_SHORT).show()
            }else if(i == R.id.radioButton48){
                hourDuration = 48
                Toast.makeText(requireContext(),"alarm is running for $hourDuration hours",Toast.LENGTH_SHORT).show()
            }else{
                hourDuration = 72
                Toast.makeText(requireContext(),"alarm is running for $hourDuration hours",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun unregisterAll(){
        for(element in alarmList){
            val notifyIntent = Intent(context,AlarmReceiver::class.java)
            var pendingIntent = PendingIntent.getBroadcast(context,element.id,notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent)
            }
            if(hourDuration == 72){
                pendingIntent = PendingIntent.getBroadcast(context,element.id+1000,notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent)
                }
                pendingIntent = PendingIntent.getBroadcast(context,element.id+2000,notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent)
                }
            }else if(hourDuration == 48){
                pendingIntent = PendingIntent.getBroadcast(context,element.id+1000,notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent)
                }
            }
        }
    }

    fun initRecyclers(){
        recycler_fav.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        recycler_fav.adapter = alarmAdaptor
    }
    private fun registerAll(){
        val notifyIntent = Intent(context,AlarmReceiver::class.java)
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for(element in alarmList){
            if(Calendar.getInstance().timeInMillis >= calenderEvent.timeInMillis){
                notifyIntent.putExtra(AppConstants.ALARM_ID,element.id)
                var time = calenderEvent.timeInMillis
                calenderEvent.timeInMillis = time.plus(AppConstants.HOUR_24_IN_SECONDS)
                var pendingIntent = PendingIntent.getBroadcast(context,element.id,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis,pendingIntent)
                Log.i(AppConstants.LOG_TAG,"twentyfour${calenderEvent.timeInMillis}")
                if(hourDuration == 72){
                    pendingIntent = PendingIntent.getBroadcast(context,element.id+1000,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS,pendingIntent)
                    Log.i(AppConstants.LOG_TAG,"fourtyeight${calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS}")
                    pendingIntent = PendingIntent.getBroadcast(context,element.id+2000,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_48_IN_SECONDS,pendingIntent)
                    Log.i(AppConstants.LOG_TAG,"sevenTwo${calenderEvent.timeInMillis+AppConstants.HOUR_48_IN_SECONDS}")
                }else if(hourDuration == 48){
                    pendingIntent = PendingIntent.getBroadcast(context,element.id+1000,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS,pendingIntent)
                    Log.i(AppConstants.LOG_TAG,"fourtyeight${calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS}")
                }
            }else{
                notifyIntent.putExtra(AppConstants.ALARM_ID,element.id)
                var pendingIntent = PendingIntent.getBroadcast(activity,element.id,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis,pendingIntent)
                Log.i(AppConstants.LOG_TAG,"twentyfour${calenderEvent.timeInMillis}")
                if(hourDuration == 72){
                    pendingIntent = PendingIntent.getBroadcast(context,element.id+1000,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS,pendingIntent)
                    Log.i(AppConstants.LOG_TAG,"fourtyeight${calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS}")
                    pendingIntent = PendingIntent.getBroadcast(context,element.id+2000,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_48_IN_SECONDS,pendingIntent)
                    Log.i(AppConstants.LOG_TAG,"sevenTwo${calenderEvent.timeInMillis+AppConstants.HOUR_48_IN_SECONDS}")
                }else if(hourDuration == 48){
                    pendingIntent = PendingIntent.getBroadcast(context,element.id+1000,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS,pendingIntent)
                    Log.i(AppConstants.LOG_TAG,"fourtyeight${calenderEvent.timeInMillis+AppConstants.HOUR_24_IN_SECONDS}")
                }
            }
        }
    }



}