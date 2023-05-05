package com.example.weatherforecast.app.screen.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.weatherforecast.R
import com.example.weatherforecast.app.screen.common.LocationPermissionScreen
import com.example.weatherforecast.app.screen.common.checkIfPermissionGranted
import com.example.weatherforecast.app.screen.common.getLastLocation
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.*
import java.io.IOException

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(
    modifier: Modifier=Modifier,
    onSaveLicked: (LatLng) ->Unit
    ){
     val context= LocalContext.current
    var permissionGranted by remember { mutableStateOf(
        checkIfPermissionGranted(context,Manifest.permission.ACCESS_FINE_LOCATION)
        ) }
    var singapore by remember { mutableStateOf(LatLng(0.0,0.0))}

    val scaffoldState = rememberScaffoldState()
    val permission =Manifest.permission.ACCESS_FINE_LOCATION

    Scaffold(
        topBar = {},
        scaffoldState = scaffoldState,

        ) {

        if(permissionGranted){
            getLastLocation(context = context, onLocation = {
                   singapore =LatLng(it.latitude, it.longitude)

            })
        }else {
            LocationPermissionScreen(
                context,
                permission,
                scaffoldState = scaffoldState,
                onPermissionGranted = {
                    permissionGranted=true
                },
                onPermissionDenied = {

                }
            )
        }
        if(singapore.latitude!=0.0) {
            MapContent(
                modifier, context, singapore,
                onSaveLicked = {
                    onSaveLicked.invoke(it)
                },
            )
        }
    }
}

 @Composable
fun MapContent(
     modifier: Modifier,
     context: Context,
     singapore: LatLng,
     onSaveLicked:(LatLng)->Unit,

     ) {
     var searchValue by remember { mutableStateOf("") }

     var isMapLoaded by remember { mutableStateOf(false) }

     val uiSettings by remember { mutableStateOf(MapUiSettings()) }
     val properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }

     val cameraPositionState = rememberCameraPositionState {
         position = CameraPosition.fromLatLngZoom(singapore, 8f)
     }

     val singaporeState = rememberMarkerState(position = singapore)

     var circleCenter by remember { mutableStateOf(singapore) }
     if (singaporeState.dragState == DragState.END) {
         circleCenter = singaporeState.position

     }

        Box(modifier = modifier.fillMaxSize()) {
            GoogleMap(
                cameraPositionState = cameraPositionState,
                modifier = Modifier.matchParentSize(),
                properties = properties,
                uiSettings = uiSettings,
                onMapLoaded = {
                    isMapLoaded = true

                },
                onMapClick = {
                    singaporeState.position = it

                },
                onMapLongClick = {
                    singaporeState.position = it
                },
            ) {
                if(isMapLoaded) {
                    MarkerInfoWindowContent(
                        state =MarkerState(position =singaporeState.position),
                        title = singaporeState.position.latitude.toString(),
                        draggable = true,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),

                        ) {

                        Column(verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("selected Location ", color = Color.Red)
                            Text(it.title ?: "Title", color = Color.Red)
                        }
                    }

                    Circle(
                        center = circleCenter,
                        fillColor = MaterialTheme.colors.secondary,
                        strokeColor = MaterialTheme.colors.secondaryVariant,
                        strokeWidth = 3f,
                        radius = 500.0,
                    )


                }

            }

            SearchBar(
                searchValue,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                onValueChange = { searchValue = it},
                onSearch = {
                    CoroutineScope(Dispatchers.Main).launch {
                    getMapLocation(context,it, onGetAddress = {latLng->
                        singaporeState.position = latLng
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                    })
                }
                }
            )

            if(isMapLoaded) {
              Button(
                  onClick = {
                      onSaveLicked.invoke(singaporeState.position)

                  },
                  modifier
                      .align(Alignment.BottomCenter)
                      .padding(bottom = 16.dp)
              ) {
                  Text(text = stringResource(id = R.string.save_location))
              }

          }



        }}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier
) {
    var searchJob by remember { mutableStateOf<Job?>(null) }

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
            searchJob?.cancel()
            searchJob = CoroutineScope(Dispatchers.Default).launch {
                delay(500) // debounce interval
                onSearch(newValue)
            }
        },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "emailIcon") },
        shape = RoundedCornerShape(32),
        modifier = modifier,
        placeholder = { Text(text = stringResource(id = R.string.search_her)) },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
        keyboardOptions = KeyboardOptions(
            autoCorrect = true,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(value)
            }
        )
    )
}
private  suspend  fun getMapLocation(
    context: Context,
    searchQuery: String,
    onGetAddress: (LatLng) -> Unit) {
   withContext(Dispatchers.Main) {
        var addressList: List<Address>? = null

        if (searchQuery.isNotEmpty()) {
            val geocoder = Geocoder(context)
            try {
                addressList = geocoder.getFromLocationName(searchQuery, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (addressList != null && addressList.isNotEmpty()) {
                val address: Address = addressList[0]
                val latLng = LatLng(address.latitude, address.longitude)
                onGetAddress.invoke(latLng)
            }
        }
   }
}




