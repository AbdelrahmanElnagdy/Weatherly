package com.example.weatherly.model.data

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.weatherly.model.data.TypeConverters.WeatherItemTypeConverter
import com.example.weatherly.model.remote.WeatherItem
import com.google.gson.annotations.SerializedName
@JvmSuppressWildcards
@TypeConverters(WeatherItemTypeConverter::class)
data class HourlyItem(

		@field:SerializedName("temp")
	val temp: Double? = null,

		@field:SerializedName("visibility")
	val visibility: Int? = null,

		@field:SerializedName("uvi")
	val uvi: Double? = null,

		@field:SerializedName("pressure")
	val pressure: Int? = null,

		@field:SerializedName("clouds")
	val clouds: Int? = null,

		@field:SerializedName("feels_like")
	val feelsLike: Double? = null,

		@field:SerializedName("dt")
	val dt: Int? = null,

		@field:SerializedName("pop")
	val pop: Double? = null,

		@field:SerializedName("wind_deg")
	val windDeg: Int? = null,

		@field:SerializedName("dew_point")
	val dewPoint: Double? = null,

		@field:SerializedName("weather")
	val weather: List<WeatherItem?>? = null,

		@field:SerializedName("humidity")
	val humidity: Int? = null,

		@field:SerializedName("wind_speed")
	val windSpeed: Double? = null,

		@field:SerializedName("snow")
	val snow: Snow? = null,

		@field:SerializedName("rain")
	val rain: Rain? = null
)