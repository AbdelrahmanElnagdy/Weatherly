package com.example.weatherly.model.data.TypeConverters

import androidx.room.TypeConverter
import com.example.weatherly.model.data.AlertsItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlertItemTypeConverter {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromAlertItemList(value: MutableList<AlertsItem?>?): String? {
            val gson = Gson()
            val type = object : TypeToken<MutableList<AlertsItem>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toAlertItemList(value: String?): MutableList<AlertsItem?>? {
            if (value == null) {
                return null
            }
            val gson = Gson()
            val type = object : TypeToken<MutableList<AlertsItem>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}