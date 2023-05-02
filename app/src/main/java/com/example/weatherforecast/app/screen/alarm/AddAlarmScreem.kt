package com.example.weatherforecast.app.screen.alarm

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecast.R
import com.example.weatherforecast.app.screen.map.MapScreen
import com.example.weatherforecast.domain.models.db.Alarm
import com.google.android.gms.maps.model.LatLng
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun CreateAlarm1(
    modifier: Modifier=Modifier,
    alarmViewModel: AlarmViewModel= hiltViewModel(),
){
    Column() {
        Row(modifier.clickable {

            }) {
            Icon(imageVector =Icons.Default.Notifications , contentDescription = "")
            Text(text = stringResource(id = R.string.alarm_time))
           }
        Row() {
            Icon(imageVector =Icons.Default.Place , contentDescription = "")
            Text(text = stringResource(id = R.string.selecte_place))
        }

        Button(onClick = {

        }) {
            Text(text = stringResource(id = R.string.save_alarm))
        }


    }
}

@Composable
fun AlarmMap() {


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateAlarm(
    modifier: Modifier=Modifier,
    alarmViewModel: AlarmViewModel= hiltViewModel(),
    backToAlarmsScreen:()->Unit

){
    var context= LocalContext.current

    val scaffoldState = rememberBackdropScaffoldState(
       initialValue =  BackdropValue.Revealed,
    )
    val scope = rememberCoroutineScope()
     var location by remember {
         mutableStateOf("")
     }
    var latLng by remember {
        mutableStateOf(LatLng(0.0,0.0))
    }
    BackdropScaffold(
        scaffoldState = scaffoldState,
        appBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.create_alarm)) },
                navigationIcon = {
                    if (scaffoldState.isConcealed) {
                        IconButton(
                            onClick = {
                                scope.launch { scaffoldState.reveal() }
                            }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "close"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                scope.launch { scaffoldState.conceal() }
                            }
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "location"
                            )
                        }
                    }
                },
                elevation = 0.dp,
                backgroundColor = Color.Transparent
            )
        },
        backLayerBackgroundColor = MaterialTheme.colors.background,
        backLayerContent = {
            SheetContent(
                onSaveClick = {time,date->
                  alarmViewModel.addAlarm(latLng =latLng ,Alarm(time= time,date=date, place = location))
                    backToAlarmsScreen.invoke()


                },
            onCancelClick = {},
            onSelecteLocationClick={
                scope.launch { scaffoldState.conceal() }
            },
            locationValueChange = location)
        },
        frontLayerContent = {
           MapScreen(onSaveLicked = {
               latLng=it
              location=getAddressFromLatLng(it.latitude,it.longitude,context)
               scope.launch { scaffoldState.reveal() }
           })

        }
    )
}

@Composable
fun SheetContent(
    modifier: Modifier=Modifier,
    onSaveClick:(String,String)->Unit,
    onCancelClick:()->Unit, onSelecteLocationClick:()->Unit,
    locationValueChange:String
) {

    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var pickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(pickedDate)
        }
    }
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm a")
                .format(pickedTime)
        }
    }
    var alarmTime by remember { mutableStateOf("") }
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()


    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.heightIn(min = 260.dp)
            .padding(horizontal = 32.dp)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        //Text(text = stringResource(id = R.string.create_alarm))
        TextField(
            value = alarmTime, onValueChange = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = ""
                )
            },
            placeholder = { Text(text = stringResource(id = R.string.alarm_time)) },
            modifier= modifier
                .fillMaxWidth()
                .clickable {
                    dateDialogState.show()
                },
            enabled = false,




            )
        TextField(
            value = locationValueChange, onValueChange = {},
            leadingIcon = {
                Icon(imageVector =Icons.Default.Place , contentDescription = "")
            },
            placeholder = { Text(text = stringResource(id = R.string.selecte_place)) },
            modifier= modifier.fillMaxWidth().clickable {
                onSelecteLocationClick.invoke()
            },
            enabled = false

        )
        Row(modifier.fillMaxWidth().padding(top = 16.dp),horizontalArrangement = Arrangement.SpaceAround) {
            Button(

                onClick = {
                    alarmTime = ""
                    onSaveClick.invoke(formattedTime,formattedDate)
                }) {
                Text(text = stringResource(id = R.string.save), modifier = modifier.padding(horizontal = 8.dp))
            }

            Button(onClick = {
                alarmTime = ""
                onCancelClick.invoke()
            }) {
                Text(text = stringResource(id = R.string.cancle))
            }
        }


        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    timeDialogState.show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date",
                allowedDateValidator = {
                    it.dayOfMonth % 2 == 1
                }
            ) {
                pickedDate = it
                alarmTime = formattedDate
            }
        }

        MaterialDialog(
            dialogState = timeDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                }
                negativeButton(text = "Cancel")
            }
        ) {
            timepicker(
                initialTime = LocalTime.now(),
                title = "Pick a time",

            ) {
                pickedTime = it
                alarmTime = "Day  $formattedDate \nHour  $formattedTime"
            }
        }


    }
}

fun getAddressFromLatLng(lat:Double,lon:Double,context: Context): String {
    val geocoder = Geocoder(context)
    val addressList = geocoder.getFromLocation(
       lat,
        lon,
        1
    )
    val address = addressList?.get(0)
    val placeName = address?.locality

    return placeName?:""

}
