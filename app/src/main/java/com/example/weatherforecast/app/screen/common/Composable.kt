package com.example.weatherforecast.app.screen.common

import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

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

@Composable
fun LoadingScreen(modifier: Modifier=Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}



@Composable
fun AnimatedScreen(modifier: Modifier=Modifier,animation:Int) {

    val animationSpec by rememberLottieComposition(
        LottieCompositionSpec.RawRes(animation)
    )

    val progress by animateLottieCompositionAsState(
        animationSpec,
        iterations = LottieConstants.IterateForever,
        speed = 3f
    )

    Box(modifier.fillMaxSize()) {
        LottieAnimation(
            composition = animationSpec,
            progress = progress,
            modifier = Modifier.fillMaxSize().align(Alignment.Center)
        )
    }
}