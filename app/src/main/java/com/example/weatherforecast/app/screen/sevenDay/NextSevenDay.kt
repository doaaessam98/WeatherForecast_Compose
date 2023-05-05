package com.example.weatherforecast.app.screen.sevenDay

import android.annotation.SuppressLint
import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.app.screen.alarm.CustomFloatingButton
import com.example.weatherforecast.app.screen.common.ImageFromInternet
import com.example.weatherforecast.app.screen.common.LoadingScreen
import com.example.weatherforecast.domain.models.remote.Daily

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NextSevenDay(
    modifier:Modifier = Modifier,
    days: List<Daily>,
    viewModel: DaysViewModel= hiltViewModel(),
    onBackClick:()->Unit
    ){
    val daysState = viewModel.days.collectAsState().value

    Scaffold(


        topBar = {

                TopAppBar(
                    title = { Text(stringResource(id = R.string.days_title)) },
                    elevation = 1.dp,
                    backgroundColor = Color.Transparent,
                    navigationIcon = {
                        IconButton(onClick = {onBackClick.invoke()}) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                        }
                    }
                )
            }

    ){
//        LaunchedEffect(Unit){
//            viewModel.getDaysData(placeId)
//        }

//        daysState?.let {
//            when(it){
//                is DataResult.Loading->{
//                    LoadingScreen()
//                }
//                is DataResult.Success ->{
//                    if(it.data.isEmpty()){
//
//                    }else{
//                        DaysScreenContent(
//                            days = it.data,
//                            )
//                    }
//                }
//                is DataResult.Error->{
//
//                }
//            }
//        }
        
        DaysScreenContent(days = days)


    }
}



@Composable
fun DaysScreenContent(modifier: Modifier=Modifier,days:List<Daily>){

    LazyColumn(modifier.padding(vertical = 16.dp)){
        itemsIndexed(days){index,day->
            DayCard(modifier,day,index)

        }
    }}


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
