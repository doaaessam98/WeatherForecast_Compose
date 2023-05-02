package com.example.weatherforecast.domain.useCase

import com.example.weatherforecast.domain.models.db.WeatherDB
import com.example.weatherforecast.domain.repo.WeatherRepo
import javax.inject.Inject
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.domain.models.db.Weather

class GetWeatherDataUseCase  @Inject constructor(
    private val weatherRepo: WeatherRepo)
{
    suspend  operator  fun  invoke(latitude: Double, longitude: Double): DataResult<Weather> =
        weatherRepo.getWeatherData(latitude,longitude)

}