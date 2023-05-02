package com.example.weatherforecast.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecast.domain.models.db.Alarm
import com.example.weatherforecast.domain.models.db.WeatherDB

@Database(entities = arrayOf(WeatherDB::class,Alarm::class), version =5, exportSchema = false)
@TypeConverters(Converter::class)
abstract  class WeatherDataBase:RoomDatabase(){
        abstract fun weatherDao(): WeatherDao
        abstract fun alarmDao():AlarmDao
 }
