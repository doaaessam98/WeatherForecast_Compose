package com.example.weatherforecast.app.screen.setting

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.domain.models.db.Weather
import com.example.weatherforecast.domain.useCase.GetWeatherDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingViewModel  @Inject constructor(
):ViewModel() {


    private val _selectedLanguage:MutableStateFlow<Locale> = MutableStateFlow(Locale.getDefault())
    val selectedLanguage :StateFlow<Locale> = _selectedLanguage
    fun setSelectedLanguage(language: Locale) {
        _selectedLanguage.value = language
    }
}