package com.example.weatherforecast.app.navigation

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Place
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherforecast.R
import com.example.weatherforecast.app.Utils.Constants
import com.example.weatherforecast.app.screen.alarm.AlarmScreen
import com.example.weatherforecast.app.screen.alarm.CreateAlarm
import com.example.weatherforecast.app.screen.home.HomeScreen
import com.example.weatherforecast.app.screen.mangeCities.MangeCities
import com.example.weatherforecast.app.screen.map.MapScreen
import com.example.weatherforecast.app.screen.map.MapViewModel
import com.example.weatherforecast.app.screen.setting.SettingScreen
import com.example.weatherforecast.app.screen.setting.SettingViewModel
import com.example.weatherforecast.app.screen.sevenDay.NextSevenDay
import com.example.weatherforecast.app.ui.theme.pink
import com.example.weatherforecast.domain.models.remote.Daily
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    var currentLocation = remember { mutableStateOf(Pair(0.0, 0.0)) }
   var days = listOf<Daily>()
    var isTopBarVisible by remember { mutableStateOf(true) }
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { HomeBottomBarNavigation(modifier, navController) }
    ) { paddingValues ->


        NavHost(
            navController,
            startDestination = BottomBarScreen.Home.route
        )
        {
            composable(route = BottomBarScreen.Home.route) {
                HomeScreen(
                    lat = Constants.HOME_LAT,
                    lon = Constants.HOME_LON ,
                    onNextDaysClick = {
                        days=it
                        navController.navigate(Screen.SevenDay.route)
                    },
                    onLocationClicked ={
                        //navController.navigate(Screen.Cities.route)
                     }
                )

            }


            composable(route=Screen.SevenDay.route){
                  NextSevenDay(days = days) {
                      navController.popBackStack()
                  }

            }
            composable(route=Screen.Map.route) { backStackEntry ->
                val mapViewModel: MapViewModel= hiltViewModel()
                MapScreen(){
                    mapViewModel.addCity(it)
                }
                //navController.popBackStack()
            }
            composable(route = BottomBarScreen.MangeCities.route) {
                  MangeCities(onAddClick =  {
                      navController.navigate(Screen.Map.route)
                       },
                      onPlaceClick = {lat,lag->
                           Constants.HOME_LAT=lat
                           Constants.HOME_LON = lag
                          navController.popBackStack()

                      })
            }
            composable(route = BottomBarScreen.Settings.route) {

                SettingScreen()
            }
            composable(route = BottomBarScreen.Alarm.route) {
                AlarmScreen(onAddClick = {
                    navController.navigate(Screen.CreateAlarm.route)
                })
            }
            composable(route =Screen.CreateAlarm.route) {
                CreateAlarm(){
                    navController.popBackStack()
                }
            }

        }
    }

}

@SuppressLint("SuspiciousIndentation")
    @Composable
    fun HomeBottomBarNavigation(
        modifier: Modifier=Modifier,
        navController: NavHostController) {

        val navItems = listOf(
            BottomBarScreen.Home,
            BottomBarScreen.MangeCities,
            BottomBarScreen.Alarm,
            BottomBarScreen.Settings
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route
        val bottomBarDestination = navItems.any { it.route == currentDestination }
    if (bottomBarDestination) {
        Box(
           modifier = modifier
               .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
               .clip(RoundedCornerShape(8.dp))
          ) {
            BottomNavigation(
                modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.secondary,
                elevation = 8.dp
            ) {
                navItems.forEach { screen ->
                    AddItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navController = navController,
                    )


                }
            }

        }
    }
    }
    @Composable
    fun RowScope.AddItem(
        screen: BottomBarScreen,
        currentDestination: String?,
        navController: NavHostController
    ) {

        val selected = currentDestination == screen.route
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = screen.icon,
                    tint =if(selected) pink else MaterialTheme.colors.onSecondary,
                    contentDescription = stringResource(id = screen.title)
                )
            },

            label = { Text(text = stringResource(id = screen.title)) },

            alwaysShowLabel = true,
            selected = currentDestination == screen.route,
            selectedContentColor =Color(0xFFFF8FB1),
            unselectedContentColor = Color.Black,

            onClick = {
                navController.navigate(screen.route) {

                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }

                    launchSingleTop = true

                    restoreState = true

                }
            }


        )
    }

sealed class BottomBarScreen(
    val route: String,
    @StringRes
    var title: Int,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home",
        title = R.string.home,
        icon = Icons.Default.Home
    )

    object MangeCities : BottomBarScreen(
        route = "place",
        title = R.string.place,
        icon = Icons.Rounded.Place
    )
    object Alarm : BottomBarScreen(
        route = "Alarm",
        title = R.string.alarm,
        icon =Icons.Default.Notifications
    )

    object Settings : BottomBarScreen(
        route = "settings",
        title = R.string.setting,
        icon = Icons.Default.Settings
    )

}

private fun updateBaseContextLocale(context: Context?): Context? {
    val language = getSavedLanguage()
    val configuration = context?.resources?.configuration
    Locale.setDefault(Locale(language))
    val updatedConfiguration = android.content.res.Configuration(configuration)
    updatedConfiguration.setLocale(Locale(language))
    return context?.createConfigurationContext(updatedConfiguration)
}


private fun getSavedLanguage(): String {

    return "ar"
}