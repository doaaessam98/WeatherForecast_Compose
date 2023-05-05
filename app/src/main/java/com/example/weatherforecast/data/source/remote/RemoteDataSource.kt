package com.example.weatherforecast.data.source.remote

import com.example.weatherforecast.domain.models.remote.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getWeatherData(lat: Double, lon: Double,lang:String): WeatherResponse
}

