package com.example.weatherly.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherly.model.data.TypeConverters.DaysTypeConverter


@Entity()
@TypeConverters(DaysTypeConverter::class)
data class AlertModel(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    val days:List<String>?,
    val alertType:String,
    val maxMinValue:String,
    val thresholdValue:Double
)