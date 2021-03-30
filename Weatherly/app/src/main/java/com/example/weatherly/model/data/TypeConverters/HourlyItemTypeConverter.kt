package com.example.weatherly.model.data.TypeConverters

import androidx.room.TypeConverter
import com.example.weatherly.model.data.HourlyItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HourlyItemTypeConverter {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromAlertItemList(value: MutableList<HourlyItem?>?): String? {
            val gson = Gson()
            val type = object : TypeToken<MutableList<HourlyItem>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toAlertItemList(value: String?): MutableList<HourlyItem?>? {
            if (value == null) {
                return null
            }
            val gson = Gson()
            val type = object : TypeToken<MutableList<HourlyItem>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}