package com.example.weatherforecast.domain.models.db

import androidx.room.*
import com.example.weatherforecast.domain.models.remote.Current
import com.example.weatherforecast.domain.models.remote.Daily
import com.example.weatherforecast.domain.models.remote.Hourly
import com.example.weatherforecast.domain.models.remote.Minutely


@Entity(tableName ="CurrentWeather", indices = [Index(value =["id", "lat", "lon"], unique = true)])
data class WeatherDB(
    @PrimaryKey
    val id :String ="",
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: String,
    val lon: String,
//  val minutely: List<Minutely> = emptyList(),
    val timezone: String,
    val timezoneOffset: Int
    )


fun WeatherDB.toWeatherModel() : Weather {
    return Weather(
        id = id,
        current = current,
        daily = daily,
        hourly = hourly,
        lat = lat.toDouble(),
        lon = lon.toDouble(),
        timezone = timezone,
        timezoneOffset = timezoneOffset
    )

}

