package com.example.weatherforecast.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherforecast.app.screen.map.MapViewModel
import com.example.weatherforecast.app.screen.setting.SettingViewModel
import com.example.weatherforecast.app.screen.splash.SplashScreen

import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun NavGraph(
    navController: NavHostController,

){

    val systemUiController: SystemUiController = rememberSystemUiController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        ) {
        composable(route = Screen.Splash.route) {
            systemUiController.isStatusBarVisible = false
            SplashScreen() {
                navController.navigate(Screen.BottomNav.route){
                        popUpTo(Screen.Splash.route){
                            inclusive=true
                        }


                }
            }
        }
        composable(route=Screen.BottomNav.route){
            systemUiController.isStatusBarVisible = true
            BottomNavGraph(modifier = Modifier)
        }
    }

    }
sealed class Screen(val route: String){
     object BottomNav:Screen(route = "bottom_nav")
     object Splash:Screen(route = "splash_screen")
     object  SevenDay:Screen(route = "seven_day")
     object Map:Screen(route ="map/{lat}/{lan}")
     object CreateAlarm:Screen(route ="create_alarm")


//    data class Map(val lat: Double, val lan: Double):Screen(route ="map/${lat}/${lan}")

}