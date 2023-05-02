package com.example.weatherforecast.app.screen.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.domain.models.db.Alarm
import com.example.weatherforecast.domain.useCase.AddAlarmUseCase
import com.example.weatherforecast.domain.useCase.GetAlarmsUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private  val addAlarmUseCase: AddAlarmUseCase,
    private val  getAlarmsUseCase: GetAlarmsUseCase,

) :ViewModel() {
    private  val _alarm: MutableStateFlow<DataResult<List<Alarm>>?> = MutableStateFlow(DataResult.Loading)
    val alarms =_alarm.asStateFlow()



    fun addAlarm(latLng:LatLng,alarm: Alarm){
        viewModelScope.launch (Dispatchers.IO){
            addAlarmUseCase.invoke(alarm,latLng)
        }
    }
    init {
        getAllAlarms()
    }

    private fun getAllAlarms(){
        viewModelScope.launch {
            getAlarmsUseCase.getAlarms().  catch {
                _alarm.value =DataResult.Error(it.localizedMessage)
            }.collect{cities->
                _alarm.value = DataResult.Success(cities)
            }
        }
    }

    fun deleteAlarms(alarm:Set<Int>){
         viewModelScope.launch {
             getAlarmsUseCase.deleteAlarmsById(alarm = alarm)
         }
    }

}