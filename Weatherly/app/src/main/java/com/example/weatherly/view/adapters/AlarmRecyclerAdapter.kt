package com.example.weatherly.view.adapters

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherly.R
import com.example.weatherly.model.AlarmReceiver
import com.example.weatherly.model.data.AlertModel
import com.example.weatherly.view_model.AlarmViewModel
import kotlinx.android.synthetic.main.alert_item.view.*

class AlarmRecyclerAdapter(val alarmlist: ArrayList<AlertModel>, val viewModel: AlarmViewModel) : RecyclerView.Adapter<AlarmRecyclerAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.alert_item, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.alarm_type.text = alarmlist[position].alertType
        holder.root.setOnLongClickListener(View.OnLongClickListener {
            showDeletionDialog(position)
            true
        })
    }

private fun showDeletionDialog(position: Int){
    val builder = AlertDialog.Builder(context).setCancelable(false)
    builder.setTitle("Warning")
    builder.setMessage("Are you sure you want to delete this item?")

    builder.setPositiveButton("Yes") { _, _ ->
        unregisterAlarm(alarmlist[position].id)
        viewModel.deleteAlarmById(alarmlist[position].id.toString())

    }
    builder.setNegativeButton("NO") { _, _ ->
    }
    builder.show()
}
private fun unregisterAlarm(id:Int){
    val notifyIntent = Intent(context, AlarmReceiver::class.java)
    var pendingIntent = PendingIntent.getBroadcast(context,id,notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if (alarmManager != null) {
        alarmManager.cancel(pendingIntent)
    }
    pendingIntent = PendingIntent.getBroadcast(context,id+1000,notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    if (alarmManager != null) {
        alarmManager.cancel(pendingIntent)
    }
    pendingIntent = PendingIntent.getBroadcast(context,id+2000,notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    if (alarmManager != null) {
        alarmManager.cancel(pendingIntent)
    }
}

    override fun getItemCount() = alarmlist.size

    fun setIncomingList(incomingList: List<AlertModel>) {
        alarmlist.clear()
        alarmlist.addAll(incomingList)
        notifyDataSetChanged()
    }
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val root = view.alert_item
        val alarm_type = view.alert_type


    }

    }