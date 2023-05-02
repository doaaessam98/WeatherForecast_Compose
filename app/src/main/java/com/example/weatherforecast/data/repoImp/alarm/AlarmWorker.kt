package com.example.weatherforecast.data.repoImp.alarm

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.data.utils.Constants.CHANNEL_ID
import com.example.weatherforecast.data.utils.Constants.KEY_TIME
import com.example.weatherforecast.data.utils.Constants.KEY_LAT
import com.example.weatherforecast.data.utils.Constants.KEY_LON
import com.example.weatherforecast.data.utils.Constants.KEY_PLACE
import com.example.weatherforecast.domain.useCase.GetAlarmsUseCase
import com.example.weatherforecast.domain.useCase.GetWeatherDataUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AlarmWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private  val getAlarmsUseCase: GetAlarmsUseCase

): CoroutineWorker(appContext, workerParams) {



    @SuppressLint("SuspiciousIndentation")
    override suspend fun doWork(): Result {

        val lat = inputData.getDouble(KEY_LAT,0.0)
        val lon = inputData.getDouble(KEY_LON,0.0)
        val time = inputData.getString(KEY_TIME)
        val place = inputData.getString(KEY_PLACE)
        val response= getWeatherDataUseCase.invoke(lat,lon)
            return when(response){
                 is DataResult.Success ->{
                     NotificationManager(appContext).createNotificationChannel(CHANNEL_ID,"notification_channel","alarm_notification_channel",4)
                     NotificationManager(appContext).sendNotification(
                         title ="Alarm weather at $place" ,
                         message = response.data.current.weather[0].description,
                         channelId = CHANNEL_ID,
                         notificationId = 2,
                         lat,lon
                     )
                     if(time!=null) {
                         getAlarmsUseCase.deleteAlarm(time,place?:"")
                      }
                    Result.success()
                 }
                 is DataResult.Error->{
                     Result.retry()
                 }

                else -> {
                    Result.retry()
                }
            }
         }





    }
