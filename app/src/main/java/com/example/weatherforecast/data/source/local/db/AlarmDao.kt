package com.example.weatherforecast.data.source.local.db

import androidx.room.*
import com.example.weatherforecast.domain.models.db.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAlarm(alarm: Alarm)

    @Query("SELECT * FROM AlarmTable")
    fun getAll():Flow<List<Alarm>>

    @Query("DELETE FROM AlarmTable WHERE id IN (:ids)")
    suspend fun deleteAlarmById(ids: List<Int>)

    @Query("DELETE FROM AlarmTable WHERE time = :time ")
    suspend fun deleteAlarm(time: String)

//    @Query("DELETE FROM AlarmTable WHERE time = :time AND place = :place")
//    suspend fun deleteAlarm(time: String, place: String)

}