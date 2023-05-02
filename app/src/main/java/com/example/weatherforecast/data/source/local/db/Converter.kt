package com.example.weatherforecast.data.source.local.db

import androidx.room.TypeConverter
import com.example.weatherforecast.domain.models.remote.*
import com.google.gson.Gson

class Converter {

    @TypeConverter
    fun convertCurrentToString(current: Current) = Gson().toJson(current)

    @TypeConverter
    fun convertStringToCurrent(currentString: String) = Gson().fromJson(currentString,Current::class.java)

    @TypeConverter
    fun  listHourlyToString (value:List<Hourly>): String = Gson().toJson(value)

    @TypeConverter
    fun StringToHourlyList(value: String) = Gson().fromJson(value, Array<Hourly>::class.java).toList()

    @TypeConverter
    fun  listDailyToString (value:List<Daily>) = Gson().toJson(value)

    @TypeConverter
    fun stringToDailyList(value: String) = Gson().fromJson(value, Array<Daily>::class.java).toList()


    @TypeConverter
    fun listWeatherToString(value: List<Weather>) = Gson().toJson(value)

    @TypeConverter
    fun stringToWeatherList(value: String) = Gson().fromJson(value, Array<Weather>::class.java).toList()


    @TypeConverter
    fun listMinutelyToString(value:List<Minutely>): String = Gson().toJson(value)

    @TypeConverter
    fun stringToMinutelyList(value: String) = Gson().fromJson(value, Array<Minutely>::class.java).toList()

}