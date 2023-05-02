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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.*
import com.example.weatherforecast.R
import com.example.weatherforecast.app.screen.home.AddressFromLatLng
import com.example.weatherforecast.app.screen.home.Item
import com.example.weatherforecast.app.screen.home.getHour
import com.example.weatherforecast.app.screen.home.toDate
import com.example.weatherforecast.app.screen.mangeCities.addressFromLatLng
import com.example.weatherforecast.domain.models.db.Weather
import com.example.weatherforecast.domain.models.db.WeatherDB
import com.example.weatherforecast.domain.models.remote.Daily
import com.example.weatherforecast.domain.models.remote.Hourly



@OptIn(ExperimentalMaterialApi::class)
@Composable
 fun HomeContent2(
    modifier: Modifier,
    weather: Weather,
    onNextDaysClick:(List<Daily>)->Unit,
    onLocationClicked:()->Unit
) {
    val context = LocalContext.current

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
                            Text(text = addressFromLatLng(context,weather.lat,weather.lon),
//                             fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(text = weather.current.dt.toDate(), modifier.padding(top = 4.dp))

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

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
                                .padding(8.dp)
                                .clickable {
                                    onNextDaysClick.invoke(weather.daily)
                                }
                        ) {
                            Text(
                                text = stringResource(id = R.string.next_7_days),
                                textAlign = TextAlign.End,
                                fontWeight = FontWeight.Light
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = ""
                            )
                        }

                    }}
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(1000)),
                ) {


                    LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                        itemsIndexed(weather.hourly) { index, hour ->
                            HourItem2(index, hour)
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
                        Item(
                            name = R.string.pressure,
                            icon = R.drawable.presure,
                            value = 2332,
                            unit = ""
                        )
                        Item(
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
                        Item(
                            name = R.string.wind_speed,
                            icon = R.drawable.wind,
                            value = 2332,
                            unit = ""
                        )
                        Item(
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
                        Item(
                            name = R.string.sunrise,
                            icon = R.drawable.sun_ris,
                            value = 2332,
                            unit = ""
                        )
                        Item(
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
    Box() {
        Text(
            text = temp.toString().substringBefore(".").plus("°C"),
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Normal,
        )

    }
}


@Composable
fun HourItem2(index:Int, hour: Hourly, modifier: Modifier=Modifier) {

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

        //backgroundColor = if(index==0) MaterialTheme.colors.secondary else MaterialTheme.colors.background

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
            Text(text =hour.temp.toString(),
                modifier = modifier.padding(12.dp))
        }

    }
}




@Composable
fun ImageFromInternet(modifier: Modifier=Modifier, size: Dp, url: String) {
    Box(
        modifier = modifier
    ) {
        var isImageLoading by remember { mutableStateOf(false) }
        val painter = rememberAsyncImagePainter(
            model = "https://openweathermap.org/img/w/${url}.png",
        )

        isImageLoading = when (painter.state) {
            is AsyncImagePainter.State.Loading -> true
            else -> false
        }

        Image(
            modifier = Modifier
                .size(size)
                .align(Alignment.TopStart),
            painter = painter,
            contentDescription = "current Image",
            contentScale = ContentScale.Fit,
        )
        if(isImageLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.primary,
            )
        }


    }

}



/////1
//            if(weather!=null) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = modifier.padding(top = 64.dp)
//                    ) {
//                    Icon(
//                        imageVector = Icons.Default.Place,
//                        tint = MaterialTheme.colors.error, contentDescription = ""
//                    )
//
//                    AddressFromLatLng(
//                        latitude = weather.lat,
//                        longitude = weather.lon,
//                        modifier = modifier
//                    )
//                }
//
//                Text(text = weather.current.dt.toDate())
//                Box(){
//                    ImageFromInternet(
//                        size = 150.dp,
//                        url = weather.current.weather[0].icon
//
//                    )
//                    Text(
//                        text = "33".plus("c"),
//                        fontSize = 70.sp,
//                        fontWeight = FontWeight.Normal,
//                        modifier= Modifier.padding(top = 90.dp)
//
//                    )
//                    Text(
//                        text = "o",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Normal,
//                        modifier= Modifier.padding(top = 100.dp, start = 74.dp)
//
//                    )
//
//                }
//
//                Text(
//                    text = weather.current.weather[0].description,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Normal,
//
//                )
//
//
//            }


//            Box(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(start = 15.dp)
//
//
//            ) {
//
//              Column(modifier = modifier.align(Alignment.BottomStart)) {
//                Row(
//                    verticalAlignment = Alignment.Bottom,
//
//                ) {
//                    if(weather!=null) {
//                        Text(
//                            text = weather.current.temp.toString(),
//                            fontSize = 50.sp,
//                            fontWeight = FontWeight.Normal,
//
//                        )
//                        Text(
//                            text = weather.current.weather[0].description,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Normal,
//                           modifier= modifier.padding(start = 8.dp, bottom = 4.dp)
//
//                        )
//
//                    }
//                }
//
//                  Row() {
//                    if(weather!=null) {
//                        Text(text = weather.current.dt.toDate(), modifier.padding(end = 8.dp))
//
//                        Text(text = "${weather.daily[0].temp.max} / ${weather.daily[0].temp.min} ")
//                    }
//                }
//              }
//
//
//
//                Box(
//                    modifier = modifier
//                        .align(Alignment.TopEnd).padding(bottom = 32.dp)
//                        .size(150.dp)
//                ) {
//                    var isImageLoading by remember { mutableStateOf(false) }
//                    val painter = rememberAsyncImagePainter(
//                        model = "https://openweathermap.org/img/w/${weather?.current?.weather?.get(0)?.icon}.png",
//                    )
//
//                    isImageLoading = when (painter.state) {
//                        is AsyncImagePainter.State.Loading -> true
//                        else -> false
//                    }
//
//                    Image(
//                        modifier = Modifier
//                            .size(150.dp),
//                        painter = painter,
//                        contentDescription = "current Image",
//                        contentScale = ContentScale.FillBounds,
//                    )
//                    if(isImageLoading) {
//                        CircularProgressIndicator(
//                            modifier = Modifier.align(Alignment.Center),
//                            color = MaterialTheme.colors.primary,
//                        )
//                    }
//
//
//                }
//
//            }









