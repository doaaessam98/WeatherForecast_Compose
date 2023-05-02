package com.example.weatherforecast.app.screen

import android.app.Activity
import android.app.LocalActivityManager
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.weatherforecast.app.navigation.NavGraph
import com.example.weatherforecast.app.screen.map.MapViewModel
import com.example.weatherforecast.app.ui.theme.WeatherForecastTheme
import androidx.fragment.app.activityViewModels
import com.example.weatherforecast.app.Utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent?.extras
        val lat = extras?.getDouble(Constants.KET_HOME_LAT)
        val lon = extras?.getDouble(Constants.KEY_HOME_LON)
         Constants.HOME_LAT=lat?:0.0
         Constants.HOME_LON = lon?:0.0
        setContent {
            WeatherForecastTheme {
                 navHostController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                   NavGraph(navController = navHostController)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherForecastTheme {
        Greeting("Android")
    }
}