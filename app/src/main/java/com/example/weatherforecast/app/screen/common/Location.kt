package com.example.weatherforecast.app.screen.common

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@Composable
fun getLastLocation(context: Context, onLocation:(Location)->Unit) {
    val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationState = remember { mutableStateOf<Location?>(null) }
    LaunchedEffect(Unit) {
        try {
            val location = fusedLocationProviderClient.lastLocation.await()
            locationState.value = location
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error getting location: $e")
        }
    }

    locationState.value?.let { location ->
        onLocation(location)
    }

}

