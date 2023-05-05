package com.example.weatherforecast.app.Utils

import android.text.format.DateFormat
import java.util.*

fun Int.toDate(): String {

    val timeInMillis = this.toLong() * 1000
    val dayOfTheWeek = DateFormat.format("EE", timeInMillis) as String
    val day = DateFormat.format("d", timeInMillis) as String
    val monthString = DateFormat.format("MMM",timeInMillis) as String
    return "$dayOfTheWeek $day $monthString"
}

fun Int.getHour(): String {
    val timeInMillis = this.toLong() * 1000
    val time  = DateFormat.format("hh:mm aa",timeInMillis)
    return time.toString()
}

fun Double.convertToArabic(value: Double): String {
    if(Locale.getDefault().language=="ar") {
        return (value.toString() + "")
            .replace("1", "١").replace("2", "٢")
            .replace("3", "٣").replace("4", "٤")
            .replace("5", "٥").replace("6", "٦")
            .replace("7", "٧").replace("8", "٨")
            .replace("9", "٩").replace("0", "٠")
    }else{
        return  value.toString()
    }
}