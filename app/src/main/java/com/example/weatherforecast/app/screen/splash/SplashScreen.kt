package com.example.weatherforecast.app.screen.splash

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.weatherforecast.R

@Composable
fun SplashScreen(modifier:Modifier=Modifier,navigationToHome:()->Unit){



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(64.dp)

    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
        val logoAnimationState = animateLottieCompositionAsState(composition = composition)


        LottieAnimation(

            composition = composition,
            progress = { logoAnimationState.progress
            },


            )
        if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
              navigationToHome.invoke()

        }
    }



      }
