package com.example.weatherforecast.domain.repo

import com.example.weatherforecast.domain.models.db.Alarm
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface AlarmRepo {
    fun  getAlarms():Flow<List<Alarm>>
    suspend fun addAlarm(alarm: Alarm, latLng: LatLng)
    suspend fun deleteAlarmById(alarm: Set<Int>)
    suspend fun deleteAlarm(time:String,place:String)
}