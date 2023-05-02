package com.example.weatherforecast.domain.models.db

import com.example.weatherforecast.domain.models.remote.Current
import com.example.weatherforecast.domain.models.remote.Daily
import com.example.weatherforecast.domain.models.remote.Hourly

data class Weather(
    val id :String = "",
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
//    val minutely: List<Minutely> = emptyList(),
    val timezone: String,
    val timezoneOffset: Int
)
