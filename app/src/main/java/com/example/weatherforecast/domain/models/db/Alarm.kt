package com.example.weatherforecast.domain.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "AlarmTable", indices = [Index(value = ["id", "place", "time"], unique = true)])
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id :Int = 0,
    val time:String,
    val date:String,
    val place:String)









