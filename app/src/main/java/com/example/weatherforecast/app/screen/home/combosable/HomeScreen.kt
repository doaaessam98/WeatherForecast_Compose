package com.example.weatherforecast.app.screen.home

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.example.weatherforecast.R
import com.example.weatherforecast.app.Utils.Constants
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.app.screen.common.AnimatedScreen
import com.example.weatherforecast.app.screen.common.LocationPermissionScreen
import com.example.weatherforecast.app.screen.common.getLastLocation
import com.example.weatherforecast.app.screen.home.combosable.HomeContent
import com.example.weatherforecast.domain.models.remote.Daily
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    lat:Double,
    lon:Double,
    viewModel: HomeViewModel = hiltViewModel(),
    onNextDaysClick:(List<Daily>)->Unit,
    onLocationClicked:()->Unit
    ) {

    val context= LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val bottomNavigationHeight = with(LocalDensity.current) { 32.dp.toPx() }
    var permissionGranted by remember { mutableStateOf(false) }
    var weatherState = viewModel.weatherData.collectAsState().value
    val permission =Manifest.permission.ACCESS_FINE_LOCATION




    Scaffold(
        topBar = {},
        scaffoldState = scaffoldState,
        modifier = Modifier.padding(bottom = bottomNavigationHeight.dp)

    )
    { innerPadding ->
        if(permissionGranted) {
            if(lat ==0.0) {
                getLastLocation(context = context) { location ->
                    viewModel.getWeatherData(location.latitude, location.longitude)
                }
            }else {
                viewModel.getWeatherData(lat, lon)
                Constants.HOME_LAT=0.0
                Constants.HOME_LON =0.0

            }
             }else {
                LocationPermissionScreen(
                      context = context,
                     permission,
                      scaffoldState = scaffoldState,
                      onPermissionGranted = {
                          permissionGranted = true


                      },
                      onPermissionDenied = {

                      }
                )
            }

        when(weatherState){
           is DataResult.Loading->{
              AnimatedScreen(animation = R.raw.loading)
           }
          is DataResult.Success ->{
              HomeContent(modifier = Modifier,
                  weather = weatherState.data,
                  onNextDaysClick,
                  onLocationClicked)


          }
          is DataResult.Error->{

          } }
    }
}























