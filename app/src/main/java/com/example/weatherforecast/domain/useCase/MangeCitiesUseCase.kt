package com.example.weatherforecast.domain.useCase

import com.example.weatherforecast.domain.repo.WeatherRepo
import javax.inject.Inject

class MangeCitiesUseCase @Inject constructor(
    private val repo: WeatherRepo
) {
       suspend fun getCities() =repo.getCities()
      suspend fun deleteCities(cities:Set<String>)= repo.deleteCities(cities)
}