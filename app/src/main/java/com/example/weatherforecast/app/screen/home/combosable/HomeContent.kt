package com.example.weatherforecast.app.screen.home.combosable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.weatherforecast.R
import com.example.weatherforecast.app.Utils.convertToArabic
import com.example.weatherforecast.app.Utils.getHour
import com.example.weatherforecast.app.Utils.toDate
import com.example.weatherforecast.app.screen.common.ImageFromInternet
import com.example.weatherforecast.app.screen.mangeCities.addressFromLatLng
import com.example.weatherforecast.domain.models.db.Weather
import com.example.weatherforecast.domain.models.remote.Daily
import com.example.weatherforecast.domain.models.remote.Hourly
import java.util.*


@Composable
 fun HomeContent(
    modifier: Modifier,
    weather: Weather,
    onNextDaysClick:(List<Daily>)->Unit,
    onLocationClicked:()->Unit
) {


    var context = LocalContext.current

    var visible by remember { mutableStateOf(false) }

    val animationSpec by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.clouds_loop)
        )

        val progress by animateLottieCompositionAsState(
            animationSpec,
            iterations = LottieConstants.IterateForever,
            speed = 5f
        )
    Box(
        Modifier
            .fillMaxSize()
            .padding(start = 16.dp)
       ){
        LaunchedEffect(Unit) {
            visible = true
        }
        //            LottieAnimation(
//                composition = animationSpec,
//                progress = progress,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp)
//            )
        Column(
            Modifier
                .fillMaxSize()

                .verticalScroll(rememberScrollState()),

        ) {

                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(1000)
                    ),
                ) {
//                    Card(
//                        onClick = {onLocationClicked()},
//                        shape = RoundedCornerShape(8.dp),
//                        elevation = 4.dp,
//                        modifier = modifier
//                            .padding(8.dp)
//
//                    ) {

                        Column(
                            modifier
                                .padding(vertical = 16.dp)
                                .padding(horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally) {

                                Text(
                                    text = addressFromLatLng(context, weather.lat, weather.lon),
//                             fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )

                            Text(text = weather.current.dt.toDate(), modifier.padding(top = 4.dp),
                                color =Color.Gray)

                        }


                   // }
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(1000)
                    ),
                ) {

                    //Text(text = weather.current.dt.toDate(), modifier.padding(start = 16.dp))
                }
                Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(1000)
                        ),
                    ) {

                        ImageFromInternet(
                            size = 150.dp,
                            url = weather.current.weather[0].icon
                        )
                    }


                    Column {
                        AnimatedVisibility(
                            visible = visible,
                            enter = slideInHorizontally(
                                initialOffsetX = { -it },
                                animationSpec = tween(1000)
                            ),
                        ) {
                            Temperature(temp = weather.current.temp, unit = "",fontSize = 70)
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = slideInHorizontally(
                                initialOffsetX = { -it },
                                animationSpec = tween(1000)
                            ),
                        ){
                            Text(
                                text = weather.current.weather[0].description,
                                fontSize = 20.sp,
                            )
                        }
                    }

                }

                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(1000)
                    ),
                ) {
                    Row(
                        modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.today),
                            fontSize = 24.sp,
                            textAlign = TextAlign.Start,
                        )
                        Spacer(modifier = modifier.weight(1f))

                            Text(
                                text = stringResource(id = R.string.next_7_days),
                                textAlign = TextAlign.End,
                                fontWeight = FontWeight.Light,
                                modifier = modifier.padding(end = 8.dp).clickable {
                                    onNextDaysClick.invoke(weather.daily)

                                }

                            )



                    }}
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(1000)),
                ) {


                    LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                        itemsIndexed(weather.hourly) { index, hour ->
                            HourItem(index, hour)
                        }
                    }
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically (initialOffsetY = {it}, animationSpec = tween(1000))
//                    enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(1000)),
                ) {
                    Text(text = stringResource(id = R.string.weather_details), modifier = Modifier.padding(16.dp))
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(1000)),
                ) {

                    Row(
                        modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        WeatherDetailsItem(
                            name = R.string.pressure,
                            icon = R.drawable.presure,
                            value = 2332,
                            unit = ""
                        )
                        WeatherDetailsItem(
                            name = R.string.humidity,
                            icon = R.drawable.humidity,
                            value = 2332,
                            unit = "%"
                        )

                    }
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(1000)),
                ) {
                    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        WeatherDetailsItem(
                            name = R.string.wind_speed,
                            icon = R.drawable.wind,
                            value = 2332,
                            unit = ""
                        )
                        WeatherDetailsItem(
                            name = R.string.visibility,
                            icon = R.drawable.visability,
                            value = 2332,
                            unit = ""
                        )

                    }
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(1000)),
                ) {
                    Row(
                        modifier
                            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        WeatherDetailsItem(
                            name = R.string.sunrise,
                            icon = R.drawable.sun_ris,
                            value = 2332,
                            unit = ""
                        )
                        WeatherDetailsItem(
                            name = R.string.sunset,
                            icon = R.drawable.sun_set,
                            value = 2332,
                            unit = ""
                        )

                    }

                }
            }

            }
            }


@Composable
fun Temperature(modifier: Modifier=Modifier,temp: Double, unit: String,fontSize:Int) {
    Row() {
        Text(
            text = temp.convertToArabic(temp).substringBefore("."),
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Normal,
        )
        Text(text = "°C",
            fontSize = (fontSize-8).sp,
            fontWeight = FontWeight.Normal)

    }
}

@Composable
fun HourItem(index:Int, hour: Hourly, modifier: Modifier=Modifier) {

    Card(
        modifier = modifier
            .padding(8.dp)
            .rotate(10f)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = 320.dp,
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = if(index==0) modifier.background(brush =  Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colors.onSecondary,
                        MaterialTheme.colors.secondary
                    )))
               else  modifier.background(color = MaterialTheme.colors.background)
            ) {
            Text(text =hour.dt.getHour(),
                modifier = modifier.padding(12.dp))
            ImageFromInternet(size = 50.dp, url =hour?.weather?.get(0)?.icon)
            Row() {
                Text(text =hour.temp.convertToArabic(hour.temp).plus(" °C"),
                    modifier = modifier.padding(12.dp),
                    fontSize = 16.sp
                )

            }

        }

    }
}

@Composable
fun WeatherDetailsItem(modifier: Modifier=Modifier, name: Int, icon:Int, value: Int?, unit:String) {

    (Column(horizontalAlignment = Alignment.CenterHorizontally, modifier =modifier.padding(16.dp) ) {
        Icon(painter = painterResource(id = icon), contentDescription = "")
        Text(
            text = stringResource(name),
            modifier = modifier.padding(8.dp)
        )

        Text(
            text = value.toString() + unit,
            modifier = modifier.padding(8.dp)
        )
    })


}




