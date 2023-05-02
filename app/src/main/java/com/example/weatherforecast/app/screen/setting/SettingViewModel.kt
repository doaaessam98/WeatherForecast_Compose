package com.example.weatherforecast.app.screen.setting

import androidx.lifecycle.ViewModel
import com.example.weatherforecast.domain.useCase.GetWeatherDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel  @Inject constructor(
    private val weatherDataUseCase: GetWeatherDataUseCase
):ViewModel() {
}