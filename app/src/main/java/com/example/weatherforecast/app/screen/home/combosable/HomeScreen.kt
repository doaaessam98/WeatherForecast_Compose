package com.example.weatherforecast.app.screen.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.format.DateFormat
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.weatherforecast.app.Utils.Constants
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.app.screen.home.combosable.HomeContent2
import com.example.weatherforecast.app.screen.home.combosable.checkIfPermissionGranted
import com.example.weatherforecast.domain.models.db.Weather
import com.example.weatherforecast.domain.models.db.WeatherDB
import com.example.weatherforecast.domain.models.remote.Daily
import com.example.weatherforecast.domain.models.remote.Hourly
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
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
               LoadingScreen()
           }
          is DataResult.Success ->{
              HomeContent2(modifier = Modifier,
                  weather = weatherState.data,
                  onNextDaysClick,
                  onLocationClicked)


          }
          is DataResult.Error->{

          } }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier=Modifier) {
    Column(modifier = modifier.fillMaxSize()) {

    }
}



@Composable
fun HourItem(index:Int,hour: Hourly, modifier: Modifier=Modifier) {

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 2.dp,
        modifier = modifier.padding(8.dp),
        backgroundColor = if(index==0) MaterialTheme.colors.secondary else MaterialTheme.colors.background

         ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text =hour.dt.getHour(),
                modifier = modifier.padding(12.dp))
            Box(
                modifier= modifier
                    .padding(horizontal = 8.dp)
                    .size(50.dp)
            ) {
                var isImageLoading by remember { mutableStateOf(false) }
                val painter = rememberAsyncImagePainter(
                    model ="https://openweathermap.org/img/w/${hour?.weather?.get(0)?.icon}.png",

                    )

                isImageLoading = when (painter.state) {
                    is AsyncImagePainter.State.Loading -> true
                    else -> false
                }

                Image(
                    modifier = Modifier
                        .size(120.dp),
                    painter = painter,
                    contentDescription = "hour Image",
                    contentScale = ContentScale.Crop,
                )


                if (isImageLoading) {

                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color =MaterialTheme.colors.primary,
                    )
                }


            }
            Text(text =hour.temp.toString(),
                modifier = modifier.padding(12.dp))
        }

    }
}

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DayItem(day: Daily,modifier: Modifier=Modifier,onDayClick:(Daily)->Unit) {

    Card(
        onClick = {onDayClick.invoke(day)},
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        modifier = modifier.padding(8.dp),
        ) {
        Text(text =day.dt.toDate(),
        modifier = modifier.padding(12.dp))
  }
}

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



@SuppressLint("MissingPermission")
@Composable
fun getLastLocation(context: Context,onLocation:(Location)->Unit) {
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationState = remember { mutableStateOf<Location?>(null) }
    LaunchedEffect(Unit) {
        try {
            val location = fusedLocationProviderClient.lastLocation.await()
            locationState.value = location
        } catch (e: Exception) {
            Log.e(TAG, "Error getting location: $e")
        }
    }

    locationState.value?.let { location ->
        onLocation(location)
    }

}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(
    context: Context,
    permission:String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    scaffoldState: ScaffoldState?=null,

    ) {
    val permissionState = rememberPermissionState(permission)
    var showSnackbar = remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(true) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            if (permissionState.shouldShowRationale) {

            } else {
                onPermissionDenied()
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (checkIfPermissionGranted(context,permission)) {
            onPermissionGranted()
        } else{
            onPermissionDenied()
        }
    }

    DisposableEffect(key1 = Unit) {
        if (permissionState.hasPermission) {
            onPermissionGranted()
        } else if(!permissionState.permissionRequested) {
            permissionLauncher.launch(permission)
        }else{
        }
        
        onDispose { }
    }

    if (permissionState.shouldShowRationale &&permissionState.permissionRequested&& showDialog) {
           ShowAlertDialog(
               title = "Location Permission Required",
               message ="This app requires location permission to function properly." ,
               onConfirm = {
                    showDialog = false
                     permissionState.launchPermissionRequest()
                           },
               onDismiss = {
                   showDialog = false
                   showSnackbar.value = true })
    }

    else if (permissionState.permissionRequested && !permissionState.hasPermission) {
         onPermissionDenied()
    }

     if(showSnackbar.value){
         LaunchedEffect(showSnackbar.value) {

             val snackbarResult = scaffoldState?.snackbarHostState?.showSnackbar(
                 message = "Location Permission Required",
                 actionLabel = "Grant Access",
                 duration = SnackbarDuration.Long

             )
             when (snackbarResult) {
                 SnackbarResult.Dismissed -> {
                     onPermissionDenied()
                 }
                 SnackbarResult.ActionPerformed -> {
                     val intent =
                         Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                             val packageName = context.packageName
                             data = Uri.fromParts("package", packageName, null)
                         }
                     launcher.launch(intent)
                 }
             }
         }

     }



}

@Composable
fun ShowAlertDialog(
    title:String,
    message:String,
    onConfirm:()->Unit,
    onDismiss:()->Unit
){


    AlertDialog(
        onDismissRequest = {},
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            ClickableText(
                text = AnnotatedString("ok"),
                onClick = {onConfirm()
                          onDismiss()},
                modifier = Modifier.padding(8.dp)


            )
        },
        dismissButton = {
            ClickableText(
                text = AnnotatedString("Cancel"),
                onClick = {
                    onDismiss()
                   },
                modifier = Modifier.padding(8.dp),


            )
        }
    )
}

@Composable
fun AddressFromLatLng(latitude: Double, longitude: Double) {
    val context = LocalContext.current
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }
    val addressState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val address = withContext(Dispatchers.IO) {
            try {
                val results = geocoder.getFromLocation(latitude, longitude, 1)
                if (results?.isNotEmpty()==true) {

                    results[0]
                } else {
                    null
                }
            } catch (e: IOException) {
                null
            }
        }
        addressState.value = address?.let {
            address.adminArea
        }?:""

    }

        Text(text =  addressState.value.substringBefore("Govern"),
            fontSize = 16.sp)



}

@Composable
fun Item(modifier: Modifier=Modifier, name: Int, icon:Int, value: Int?, unit:String) {

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




