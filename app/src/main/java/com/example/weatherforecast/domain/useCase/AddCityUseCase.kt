package com.example.weatherforecast.domain.useCase

import com.example.weatherforecast.domain.repo.WeatherRepo
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class AddCityUseCase  @Inject constructor(
    private val repo: WeatherRepo
) {
    suspend operator  fun invoke(latLng:LatLng) =repo.insertDataInDataBase(latLng)
}