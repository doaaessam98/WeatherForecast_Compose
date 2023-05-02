package com.example.weatherforecast.domain.models.db

import androidx.room.PrimaryKey
import com.example.weatherforecast.domain.models.remote.Current
import com.example.weatherforecast.domain.models.remote.Daily
import com.example.weatherforecast.domain.models.remote.Hourly

data class City(
    val name:String,
    val lat: Double,
    val lon: Double,
    @PrimaryKey
    val timezone: String,
    )
