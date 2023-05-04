package com.example.weatherforecast.app.screen.mangeCities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.app.screen.alarm.CustomFloatingButton
import com.example.weatherforecast.app.screen.alarm.getAddressFromLatLng
import com.example.weatherforecast.app.screen.home.AddressFromLatLng
import com.example.weatherforecast.app.screen.home.LoadingScreen
import com.example.weatherforecast.app.screen.home.ShowAlertDialog
import com.example.weatherforecast.app.screen.home.combosable.ImageFromInternet
import com.example.weatherforecast.app.screen.home.combosable.Temperature
import com.example.weatherforecast.domain.models.db.Weather
import com.example.weatherforecast.domain.models.db.WeatherDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MangeCities(
    modifier:Modifier = Modifier,
    viewModel:MangeCitiesViewModel= hiltViewModel(),
    onAddClick:()->Unit,
    onPlaceClick:(lat:Double,lan:Double)->Unit
){
    val citiesState = viewModel.cities.collectAsState().value
    val bottomNavigationHeight = with(LocalDensity.current) { 32.dp.toPx() }
    val isDeleteScreen=  remember { mutableStateOf(false) }
    val selectedList = remember { mutableStateOf(emptySet<String>()) }
    val citiesList  = remember { mutableStateOf(emptySet<String>()) }
    val enabled by remember(selectedList) {
        derivedStateOf{selectedList.value.isNotEmpty()}
    }
    var isSelectAll by remember {
        mutableStateOf(citiesList.value.isNotEmpty() && citiesList.value.size == selectedList.value.size)
    }
    LaunchedEffect(citiesList.value, selectedList.value) {
        isSelectAll = citiesList.value.isNotEmpty() && citiesList.value.size == selectedList.value.size
    }

    var showDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier.padding(bottom = bottomNavigationHeight.dp),

        topBar = {
            if(!isDeleteScreen.value){
                TopAppBar(
                    title = { Text(stringResource(id = R.string.cities)) },
                    actions = {
                        if(citiesList.value.isNotEmpty()){
                            IconButton(onClick = {isDeleteScreen.value = true}) {
                                Icon(painter = painterResource(id = R.drawable.edit_square), contentDescription ="Add" )

                            }
                        }},
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent
                )
            }else{
                TopAppBar(
                    title = {
                        Text(
                            if(selectedList.value.isEmpty())
                                stringResource(id = R.string.selected_items)
                            else "${selectedList.value.size} "+ stringResource(id =R.string.selected),
                            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    },
                    actions = {
                        Checkbox(checked = isSelectAll , onCheckedChange = {
                            if(it){
                                selectedList.value =  citiesList.value
                            }else{
                                selectedList.value = emptySet()
                            }
                        })
                    },
                    modifier=modifier.padding(horizontal = 8.dp),
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent,
                    navigationIcon = { Icon(imageVector =Icons.Filled.Close , contentDescription ="" ,modifier.clickable {
                        isDeleteScreen.value =false
                        selectedList.value= emptySet()
                    })}
                )
            }

        },

        floatingActionButton =

        {
            CustomFloatingButton(
                onclick = {
                    if(isDeleteScreen.value && enabled)
                    {
                         showDialog = true
                    }
                    else if(!isDeleteScreen.value){
                        onAddClick.invoke()
                    }
                },
                enabled = enabled ,
                isDeleteScreen = isDeleteScreen.value)
        },
        floatingActionButtonPosition =  if(!isDeleteScreen.value) FabPosition.End else FabPosition.Center
    ) { contentPadding->

     citiesState?.let {
         when(it){
             is DataResult.Loading->{
                 LoadingScreen()
             }
             is DataResult.Success ->{
                citiesList.value = it.data.map { it.id }.toSet()

                 if(it.data.isEmpty()){
                     Box(modifier.fillMaxSize()) {

                     }
                 }else{
                 CitiesScreenContent(
                     it.data,
                     onPlaceClick,
                     onPlaceLongClick = {city->
                         isDeleteScreen.value=true
                         if(! selectedList.value.contains(city.id)){
                         selectedList.value = selectedList.value + city.id
                           }else{
                             selectedList.value = selectedList.value - city.id
                           }
                                        },
                     onCheckedChange = {isChecked,city->
                         if(isChecked) {
                             selectedList.value = selectedList.value + city.id
                         } else {
                             selectedList.value = selectedList.value - city.id
                         }

                     },
                     isChecked = {
                         selectedList.value.contains(it.id)
                     },
                     isDeleteScreen = isDeleteScreen.value
                 )
             }
             }
             is DataResult.Error->{

             }
         }
     }
     if(showDialog){
     ShowAlertDialog(
         title = stringResource(id = R.string.confirm ) ,
         message = stringResource(id =R.string.delete_message ) ,
         onConfirm = {
             viewModel.deletePlaces(selectedList.value)
             showDialog = false
             Log.e(TAG, "MangeCities: ${citiesList.value}", )
             isDeleteScreen.value=false
             selectedList.value = emptySet()
         }, onDismiss = {
             showDialog = false
         })

    }
    }
}

@Composable
fun CitiesScreenContent(
    data: List<WeatherDB>,
    onPlaceClick: (lat: Double, lan: Double) -> Unit,
    onPlaceLongClick: (WeatherDB) -> Unit,
    isDeleteScreen:Boolean,
    isChecked:(WeatherDB)->Boolean,
    onCheckedChange:(Boolean,WeatherDB)->Unit

) {
    LazyColumn(){
        items(data){cityWeather->
            CityItem(
                modifier = Modifier,
                city=cityWeather,
                onPlaceClick = onPlaceClick,
                onPlaceLongClick = {onPlaceLongClick.invoke(it)},
                isDeleteScreen,
                onCheckedChange = {
                    onCheckedChange.invoke(it,cityWeather)
                },
                isChecked = isChecked(cityWeather)
            )
            }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CityItem(
    modifier: Modifier = Modifier,
    city: WeatherDB,
    onPlaceClick: (lat: Double, lan: Double) -> Unit,
    onPlaceLongClick:(WeatherDB)->Unit,
    isDeleteScreen:Boolean,
    isChecked:Boolean,
    onCheckedChange:(Boolean)->Unit,

) {
    var visible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val brush  = Brush.verticalGradient(colors = listOf(
            MaterialTheme.colors.onSecondary,
            MaterialTheme.colors.secondary))
    Box(
        modifier
            .fillMaxWidth()) {

        LaunchedEffect(Unit) {
            visible = true
        }

        Card(
            modifier =if(isDeleteScreen) modifier
                .width(300.dp)
                .heightIn(min = 100.dp)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
            else modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 4.dp),
            shape = RoundedCornerShape(16.dp) ,
            elevation = 4.dp,
            ) {

            Row(
                modifier
                    .combinedClickable(
                        onClick = {
                            if(!isDeleteScreen) {
                                onPlaceClick.invoke(city.lat.toDouble(), city.lon.toDouble())
                            } else {
                                onPlaceLongClick.invoke(city)
                            }

                        },
                        onLongClick = {
                            onPlaceLongClick.invoke(city)
                        },
                    )
                    .fillMaxSize()
                    .background(brush = brush),
                horizontalArrangement = Arrangement.SpaceAround,
                 verticalAlignment = Alignment.CenterVertically) {
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(1000)
                    ),
                ) {

                        Text(text = addressFromLatLng(context,city.lat.toDouble(),city.lon.toDouble()))

                }
              if(!isDeleteScreen) {
                  ImageFromInternet(
                      size = 50.dp,
                      url = city.current.weather[0].icon
                  )

                  Column(modifier.padding(vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                      AnimatedVisibility(
                          visible = visible,
                          enter = slideInHorizontally(
                              initialOffsetX = { it },
                              animationSpec = tween(1000)
                          ),
                      ) {
                          Temperature(temp = city.current.temp, unit = "", fontSize = 48)
                      }
                      AnimatedVisibility(
                          visible = visible,
                          enter = slideInHorizontally(
                              initialOffsetX = { it },
                              animationSpec = tween(1000)
                          ),
                      ) {

                          Text(
                              text = city.current.weather[0].description,
                              fontSize = 20.sp,
                          )
                      }
                  }
              }
            }

    }

        if(isDeleteScreen){
            Checkbox(
                checked =isChecked,
                onCheckedChange = {
                    onCheckedChange.invoke(it)
                },
                modifier = modifier.align(Alignment.CenterEnd)
            )

        }

}}



  fun addressFromLatLng(context:Context,latitude: Double, longitude: Double):String {
            val geocoder = Geocoder(context,Locale.getDefault())
            var addressList: List<Address>? = null
            return try {
                 addressList = geocoder.getFromLocation(latitude, longitude, 1)
                if (addressList?.isNotEmpty()==true) {
//                     addressList[0].locality.plus(addressList[0].adminArea.substringBefore("Govern"))
                  addressList[0].adminArea.substringBefore("Govern")

                } else {
                    ""
                }
            } catch (e: IOException) {
                ""
            }



    }











