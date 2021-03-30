package com.example.weatherly.model.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.weatherly.model.data.TypeConverters.AlertItemTypeConverter
import com.example.weatherly.model.data.TypeConverters.DailyItemTypeConverter
import com.example.weatherly.model.data.TypeConverters.HourlyItemTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["lat", "lon"])
@JvmSuppressWildcards
@TypeConverters(AlertItemTypeConverter::class, DailyItemTypeConverter::class, HourlyItemTypeConverter::class)
data class WeatherResponse(

    @field:SerializedName("alerts")
	val alerts: List<AlertsItem?>? = null,

    @field:SerializedName("current")
    @Embedded(prefix = "current_")
	val current: Current? = null,

    @field:SerializedName("timezone")
	val timezone: String? = null,

    @field:SerializedName("timezone_offset")
	val timezoneOffset: Int? = null,

    @field:SerializedName("daily")
	val daily: List<DailyItem?>? = null,

    @field:SerializedName("lon")
	val lon: Double,

    @field:SerializedName("hourly")
	val hourly: List<HourlyItem?>? = null,

    @field:SerializedName("lat")
	val lat: Double
)