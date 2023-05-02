package com.example.weatherforecast.data.source.remote

import com.example.weatherforecast.domain.models.remote.WeatherResponse
import kotlinx.coroutines.flow.Flow

////const val API_KEY="f2f9ec409c67b8498f33c2bf4c7fb7e7"
interface RemoteDataSource {
    suspend fun getWeatherData(lat: Double, lon: Double): WeatherResponse
}

