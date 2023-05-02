package com.example.weatherforecast.domain.useCase

import com.example.weatherforecast.domain.repo.AlarmRepo
import javax.inject.Inject

class GetAlarmsUseCase @Inject constructor(
    private val alarmRepo: AlarmRepo
) {
     fun getAlarms()=alarmRepo.getAlarms()
    suspend fun deleteAlarmsById(alarm:Set<Int>) = alarmRepo.deleteAlarmById(alarm = alarm)
    suspend fun deleteAlarm(time:String,place:String) = alarmRepo.deleteAlarm(time, place)

}