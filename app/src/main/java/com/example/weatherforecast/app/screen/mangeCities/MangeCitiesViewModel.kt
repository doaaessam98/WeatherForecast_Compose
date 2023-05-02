package com.example.weatherforecast.app.screen.mangeCities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.domain.models.db.WeatherDB
import com.example.weatherforecast.domain.useCase.MangeCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangeCitiesViewModel @Inject constructor(
    private  val mangeCitiesUseCase:MangeCitiesUseCase)
    :ViewModel() {


    private  val _cities: MutableStateFlow<DataResult<List<WeatherDB>>?> = MutableStateFlow(DataResult.Loading)
    val cities =_cities.asStateFlow()


   init {
       getCities()
   }
    private fun getCities(){
        viewModelScope.launch {
            mangeCitiesUseCase.getCities().
            catch {
              _cities.value =DataResult.Error(it.localizedMessage)
            }.collect{cities->
                _cities.value = DataResult.Success(cities.map { it})
            }
        }
    }

    fun deletePlaces(selectedList:Set<String>) {

        viewModelScope.launch {
            mangeCitiesUseCase.deleteCities(selectedList)
        }
    }


}