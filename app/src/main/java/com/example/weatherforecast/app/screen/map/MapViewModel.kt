package com.example.weatherforecast.app.screen.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.domain.useCase.AddCityUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
   private val addCityUseCase: AddCityUseCase
):ViewModel() {


    private val _lat : MutableStateFlow<Double> = MutableStateFlow(0.0)
     val lat = _lat.asStateFlow()

    private val _lon : MutableStateFlow<Double> = MutableStateFlow(0.0)
    val lon = _lon.asStateFlow()


    fun updateLocation(lat: Double, longitude: Double) {
             _lat.value= lat
        _lon.value = longitude
    }
     fun addCity(latLon:LatLng){
            viewModelScope.launch {
                addCityUseCase.invoke(latLon).let {

                }
            }

     }

}