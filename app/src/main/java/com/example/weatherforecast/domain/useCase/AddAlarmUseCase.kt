package com.example.weatherforecast.domain.useCase

import com.example.weatherforecast.domain.models.db.Alarm
import com.example.weatherforecast.domain.repo.AlarmRepo
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class AddAlarmUseCase @Inject constructor(
    private val alarmRepo: AlarmRepo
) {
    suspend operator  fun  invoke (alarm: Alarm, latLng: LatLng) = alarmRepo.addAlarm(alarm,latLng)
  }