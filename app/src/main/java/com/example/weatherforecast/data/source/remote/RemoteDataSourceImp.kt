package com.example.weatherforecast.data.source.remote

import android.content.ContentValues.TAG
import android.util.Log
import com.example.weatherforecast.data.source.remote.api.WeatherApiService
import com.example.weatherforecast.domain.models.remote.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSourceImp @Inject constructor(
    private val apiService: WeatherApiService
    ):RemoteDataSource {

    override suspend fun getWeatherData(lat:Double,lon:Double,lang:String): WeatherResponse {
       return  apiService.getWeatherData(lat,lon,lang)
    }
}