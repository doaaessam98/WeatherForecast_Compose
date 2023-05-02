package com.example.weatherforecast.data.source.local

import android.content.ContentValues.TAG
import android.util.Log
import com.example.weatherforecast.data.source.local.db.AlarmDao
import com.example.weatherforecast.data.source.local.db.WeatherDao
import com.example.weatherforecast.domain.models.db.Alarm
import com.example.weatherforecast.domain.models.db.WeatherDB
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImp @Inject constructor(
    private  val  weatherDao: WeatherDao,
    private val alarmDao: AlarmDao
):LocalDataSource {

    override suspend fun insertWeatherData(weatherDB: WeatherDB) {
       weatherDao.insertWeatherData(weatherDB)
    }

    override suspend fun getWeatherData(lat:Double,lan:Double): WeatherDB {
        return weatherDao.getWeatherData()
    }

    override fun getCities(): Flow<List<WeatherDB>> = weatherDao.getCities()
    override suspend fun deleteCities(cities: Set<String>)  = weatherDao.deleteCities(cities)
    override suspend fun addAlarm(alarm: Alarm) =alarmDao.addAlarm(alarm)
    override fun getAlarms(): Flow<List<Alarm>> =alarmDao.getAll()
    override suspend fun deleteAlarmById(alarm: Set<Int>) {
        alarmDao.deleteAlarmById(alarm.toList())
    }

    override suspend fun deleteAlarm(time: String, place: String)  = alarmDao.deleteAlarm(time)
}