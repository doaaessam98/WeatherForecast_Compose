package com.example.weatherforecast.app.screen.home


import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.domain.useCase.GetWeatherDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.weatherforecast.domain.models.db.WeatherDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.domain.models.db.Weather

@HiltViewModel
class HomeViewModel @Inject constructor(
    private  val getWeatherData: GetWeatherDataUseCase
):ViewModel(){

  private  val _weatherData: MutableStateFlow<DataResult<Weather>?> =MutableStateFlow(DataResult.Loading)
    val weatherData =_weatherData.asStateFlow()

      fun getWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            getWeatherData.invoke(latitude,longitude).let {
                when(it){
                    is DataResult.Success->{
                        Log.e(TAG, "getWeatherData: ${it.data}", )
                      _weatherData.value = it
                    }
                    is  DataResult.Error->{
                        Log.e(TAG, "getWeatherData333: ${it.message}", )


                    }

                }
            }
        }
    }

}

