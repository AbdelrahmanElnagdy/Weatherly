package com.example.weatherly.model.data.TypeConverters

import androidx.room.TypeConverter
import com.example.weatherly.model.data.AlertsItem
import com.example.weatherly.model.data.DailyItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DailyItemTypeConverter {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromAlertItemList(value: MutableList<DailyItem?>?): String? {
            val gson = Gson()
            val type = object : TypeToken<MutableList<DailyItem>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toAlertItemList(value: String?): MutableList<DailyItem?>? {
            if (value == null) {
                return null
            }
            val gson = Gson()
            val type = object : TypeToken<MutableList<DailyItem>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}