package com.example.weatherforecast.app.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.material.*

@Composable
fun SplashScreen(navigationToHome:()->Unit){

    Button(onClick = navigationToHome) {
              Text(text = "go")

          }
      }
