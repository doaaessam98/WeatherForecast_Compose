package com.example.weatherforecast.app.screen.sevenDay

import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecast.app.screen.home.combosable.ImageFromInternet
import com.example.weatherforecast.domain.models.remote.Daily

@Composable
fun NextSevenDay(modifier:Modifier = Modifier,days:List<Daily>){
    Column(modifier.padding(8.dp)) {
        Text(text = "Weather in next 7 Days",modifier.padding(bottom = 16.dp))
  LazyColumn(){
      itemsIndexed(days){index,day->
          DayCard(modifier,day,index)

      }
  }}
}

@Composable
fun DayCard(modifier:Modifier,days: Daily,index:Int) {
    Box(modifier.fillMaxWidth()) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .rotate(if(index % 2==0) 10f else -01f),
            shape = if(index % 2 ==0)RoundedCornerShape(topStartPercent =32, bottomEndPercent = 64)else RoundedCornerShape(topEndPercent =64, bottomStartPercent = 32) ,
            elevation = 20.dp,


            ) {
            Row(modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceEvenly) {
                  Column(verticalArrangement = Arrangement.Center, modifier = modifier.padding(horizontal = 8.dp)) {
                      Text(text = days.dt.getDay(),
                          fontSize = 16.sp)
                      Text(text = days.dt.getDate())
                  }
                   Column(verticalArrangement = Arrangement.SpaceAround) {
                       Text(text = days.temp.day.toString(),
                           fontSize = 24.sp)
                       Text(text = days.weather[0].description)
                   }

            ImageFromInternet(size = 120.dp, url =days.weather[0].icon)

            }
        }


    }
}

private fun Int.getDay(): String {

    val timeInMillis = this.toLong() * 1000

    return DateFormat.format("EEEE", timeInMillis) as String
}

private fun Int.getDate(): String {
    val timeInMillis = this.toLong() * 1000
    val day = DateFormat.format("d", timeInMillis) as String
    val monthString = DateFormat.format("MMM",timeInMillis) as String
    return "$day $monthString"

}
