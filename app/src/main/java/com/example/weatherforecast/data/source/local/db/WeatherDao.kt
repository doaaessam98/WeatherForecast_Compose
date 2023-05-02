package com.example.weatherforecast.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.domain.models.db.WeatherDB
import com.example.weatherforecast.domain.models.remote.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
       @Insert(onConflict = OnConflictStrategy.REPLACE)
      fun insertWeatherData(weatherDB: WeatherDB)


     @Query("SELECT * FROM currentweather ")
     suspend fun getWeatherData(): WeatherDB

    @Query("SELECT * FROM currentweather ")
    fun getCities(): Flow<List<WeatherDB>>
    @Query("DELETE FROM currentweather WHERE id IN (:citiesIds)")
    suspend fun deleteCities(citiesIds: Set<String>)
}