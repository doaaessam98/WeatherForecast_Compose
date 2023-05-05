package com.example.weatherforecast.app.screen.common

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(
    context: Context,
    permission:String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    scaffoldState: ScaffoldState?=null,

    )
{
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

    if (permissionState.shouldShowRationale &&permissionState.permissionRequested&&showDialog) {
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

fun checkIfPermissionGranted(context: Context, permission: String): Boolean {
    return (ContextCompat.checkSelfPermission(context, permission)
            ==PackageManager.PERMISSION_GRANTED)
}