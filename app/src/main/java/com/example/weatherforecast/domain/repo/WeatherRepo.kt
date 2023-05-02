package com.example.weatherforecast.domain.repo

import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.domain.models.db.Weather
import com.example.weatherforecast.domain.models.db.WeatherDB
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {
    suspend fun  refreshWeatherData(lat: Double, lon: Double)
    suspend fun  insertDataInDataBase(latLng: LatLng)
    suspend fun  getWeatherData(latitude: Double, longitude: Double): DataResult<Weather>
    suspend fun getCities(): Flow<List<WeatherDB>>
    suspend fun deleteCities(cities: Set<String>)
}
