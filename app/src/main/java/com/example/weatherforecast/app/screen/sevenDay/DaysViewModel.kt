package com.example.weatherforecast.app.screen.sevenDay

import androidx.lifecycle.ViewModel
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.domain.models.db.Weather
import com.example.weatherforecast.domain.models.db.WeatherDB
import com.example.weatherforecast.domain.models.remote.Daily
import com.example.weatherforecast.domain.useCase.GetWeatherDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DaysViewModel @Inject constructor(
    private val getWeatherDataUseCase: GetWeatherDataUseCase
):ViewModel() {

    private  val _days: MutableStateFlow<DataResult<List<Daily>>?> = MutableStateFlow(
        DataResult.Loading)
    val days =_days.asStateFlow()

    fun getDaysData(placeId: String) {

    }
}