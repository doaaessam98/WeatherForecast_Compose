package com.example.weatherforecast.domain.models.remote


import com.example.weatherforecast.domain.models.db.WeatherDB
import com.example.weatherforecast.domain.models.remote.Current
import com.example.weatherforecast.domain.models.remote.Daily
import com.example.weatherforecast.domain.models.remote.Hourly
import com.example.weatherforecast.domain.models.remote.Minutely
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("current")
    val current: Current,
    @SerialName("daily")
    val daily: List<Daily>,
    @SerialName("hourly")
    val hourly: List<Hourly>,
    @SerialName("lat")
    val lat: Double,
    @SerialName("lon")
    val lon: Double,
//    @SerialName("minutely")
//    val minutely: List<Minutely>,
    @SerialName("timezone")
    val timezone: String,
    @SerialName("timezone_offset")
    val timezoneOffset: Int
){
    fun toWeatherDataBase() : WeatherDB {
        return WeatherDB(id = lat.toString().plus(lon.toString()), current =current, daily = daily, hourly = hourly, lat = lat.toString(), lon =  lon.toString(), timezone = timezone, timezoneOffset = timezoneOffset)

    }
}