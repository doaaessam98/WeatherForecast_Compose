package com.example.weatherforecast.app.screen.home.combosable

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager.*
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

@Composable
 fun PermissionUI1(
    scaffoldState: ScaffoldState,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val  permission = Manifest.permission.ACCESS_FINE_LOCATION
//    var performLocationAction = locationPermissionViewModel.performLocationAction.collectAsState().value

    if(true){
        PermissionUI(context, permission =permission , "location permission relational",scaffoldState)
        {permissionAction ->
            when (permissionAction) {
                is PermissionAction.OnPermissionGranted -> {
//                    locationPermissionViewModel.setPerformLocationAction(false)

                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Location permission granted!")
                    }
                }
                is PermissionAction.OnPermissionDenied -> {
//                    locationPermissionViewModel.setPerformLocationAction(true)
//                    performLocationAction = locationPermissionViewModel.performLocationAction.value




                }
            }

        }

    }
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Permission Test")

        Button(onClick = {

        }) {
            Text("Capture Location")
        }
    }


}








@Composable
fun PermissionUI(
    context: Context,
    permission: String,
    permissionRationale: String,
    scaffoldState: ScaffoldState,
    permissionAction: (PermissionAction) -> Unit
) {


    val permissionGranted = checkIfPermissionGranted(
            context,
            permission
        )

    val showPermissionRationale =shouldShowPermissionRationale(
        context,
        permission
    )



    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Permission provided by user")
            // Permission Accepted
            permissionAction(PermissionAction.OnPermissionGranted)
        } else {
            Log.d(TAG, " nooooPermission provided by user")

            permissionAction(PermissionAction.OnPermissionDenied)
        }
    }

    if (permissionGranted) {
        Log.d(TAG, "Permission already granted, exiting..")
    }else{
        SideEffect {
            launcher.launch(permission)
        }
    }


    if (showPermissionRationale) {
        Log.d(TAG, "Showing permission rationale for $permission")

        LaunchedEffect(showPermissionRationale) {

            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = permissionRationale,
                actionLabel = "Grant Access",
                duration = SnackbarDuration.Long

            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    Log.d(TAG, "User dismissed permission rationale for $permission")
                    //User denied the permission, do nothing
                    permissionAction(PermissionAction.OnPermissionDenied)
                }
                SnackbarResult.ActionPerformed -> {
                    Log.d(TAG, "User granted permission for $permission rationale. Launching permission request..")
                    launcher.launch(permission)
                }
            }
        }
    } else {
        //Request permissions again
        Log.d(TAG, "Requesting permission for $permission")
        SideEffect {
            launcher.launch(permission)
        }

    }


}


fun checkIfPermissionGranted(context: Context, permission: String): Boolean {
    return (ContextCompat.checkSelfPermission(context, permission)
            == PERMISSION_GRANTED)
}

fun shouldShowPermissionRationale(context: Context, permission: String): Boolean {

    val activity = context as Activity?
    if (activity == null)
        Log.d(TAG, "Activity is null")

    return ActivityCompat.shouldShowRequestPermissionRationale(
        activity!!,
        permission
    )
}


sealed class PermissionAction {

    object OnPermissionGranted : PermissionAction()

    object OnPermissionDenied : PermissionAction()
}