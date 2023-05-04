package com.example.weatherforecast.data.source.remote.api

import com.example.weatherforecast.domain.models.remote.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {



    ///const val EXCLUDE="minutely"

@GET("data/2.5/onecall")
suspend fun getWeatherData(
    @Query("lat")lat:Double,
    @Query("lon")lon:Double,
    @Query("units")unit:String ="metric" ,
    @Query("appid")apiId:String=""
):WeatherResponse

}