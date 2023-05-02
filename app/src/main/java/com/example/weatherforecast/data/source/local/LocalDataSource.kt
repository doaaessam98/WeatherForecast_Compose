package com.example.weatherforecast.data.source.local

import com.example.weatherforecast.domain.models.db.Alarm
import com.example.weatherforecast.domain.models.db.Weather
import com.example.weatherforecast.domain.models.db.WeatherDB
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

  suspend  fun insertWeatherData(weatherDB: WeatherDB)

  fun getCities(): Flow<List<WeatherDB>>

  suspend fun getWeatherData(lat: Double, lan: Double): WeatherDB
  fun  getAlarms():Flow<List<Alarm>>
   suspend fun addAlarm(alarm: Alarm)
    suspend fun deleteAlarmById(alarm: Set<Int>)
    suspend fun deleteAlarm(time: String, place: String)
    suspend fun deleteCities(cities: Set<String>)


}