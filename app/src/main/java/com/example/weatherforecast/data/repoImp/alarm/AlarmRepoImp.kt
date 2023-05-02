package com.example.weatherforecast.data.repoImp.alarm

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherforecast.data.source.local.LocalDataSource
import com.example.weatherforecast.data.utils.Constants.KEY_TIME
import com.example.weatherforecast.data.utils.Constants.KEY_LAT
import com.example.weatherforecast.data.utils.Constants.KEY_LON
import com.example.weatherforecast.data.utils.Constants.KEY_PLACE
import com.example.weatherforecast.domain.models.db.Alarm
import com.example.weatherforecast.domain.repo.AlarmRepo
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AlarmRepoImp @Inject constructor(
    private  val localDataSource: LocalDataSource,
    private  val workManager: WorkManager


) :AlarmRepo{

    override fun getAlarms(): Flow<List<Alarm>> =localDataSource.getAlarms()

    override suspend fun addAlarm(alarm: Alarm, latLng: LatLng) =
        withContext(Dispatchers.IO){
        localDataSource.addAlarm(alarm)
         saveAlarmToWorkManger(latLng,alarm)
        }

    override suspend fun deleteAlarmById(alarm: Set<Int>)  = localDataSource.deleteAlarmById(alarm)
    override suspend fun deleteAlarm(time: String, place: String) = localDataSource.deleteAlarm(time,place)

    private fun saveAlarmToWorkManger(latLng: LatLng, alarm: Alarm) {

        val currentDateFormat = SimpleDateFormat("MMM dd yyyy HH:mm a", Locale.ENGLISH)
        val formattedDateString = currentDateFormat.format(Date())
        val formattedDate = currentDateFormat.parse(formattedDateString)
        val currentTimeInMillis = formattedDate.time


        //convert ime 12 to 24 hour
        val inputFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val inputTime = alarm.time
        val calendar = Calendar.getInstance()
        val time = inputFormat.parse(inputTime)
        calendar.time = time
        val outputTime = outputFormat.format(calendar.time)

        val dateFormat = SimpleDateFormat("MMM dd yyyy HH:mm", Locale.US)
        val dateTimeString = "${alarm.date} ${outputTime}"
        val date = dateFormat.parse(dateTimeString)
        var endDate = date.time

        var initialDelay = (endDate / 1000L) - (currentTimeInMillis / 1000L)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val alarmWorkMangerRequest = OneTimeWorkRequestBuilder<AlarmWorker>().
            setInitialDelay(initialDelay,TimeUnit.SECONDS).
            setInputData(createInputDataForWorkRequest(alarm,latLng))
           // setConstraints(constraints)
            .addTag(alarm.time)
            .build()
        workManager.enqueue(alarmWorkMangerRequest)

      }

     fun cancelWork(alarm: Alarm) {
        workManager.cancelUniqueWork(alarm.time)
    }

    private fun createInputDataForWorkRequest(alarm: Alarm,latLng: LatLng): Data {
        val builder = Data.Builder()
        builder.putDouble(KEY_LAT,latLng.latitude).
        putDouble(KEY_LON ,latLng.longitude).
        putString(KEY_PLACE,alarm.place).
        putString(KEY_TIME,alarm.time)
        return builder.build()
    }

}