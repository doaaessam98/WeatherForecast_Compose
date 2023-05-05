package com.example.weatherforecast.app.screen

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.weatherforecast.app.Utils.Constants
import com.example.weatherforecast.app.navigation.NavGraph
import com.example.weatherforecast.app.screen.setting.SettingViewModel
import com.example.weatherforecast.app.ui.theme.WeatherForecastTheme
import com.example.weatherforecast.data.source.local.datastore.StoreLanguage
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import androidx.compose.ui.unit.LayoutDirection
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController
   @Inject
    lateinit var  dataStore:StoreLanguage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent?.extras
        val lat = extras?.getDouble(Constants.KET_HOME_LAT)
        val lon = extras?.getDouble(Constants.KEY_HOME_LON)
         Constants.HOME_LAT=lat?:0.0
         Constants.HOME_LON = lon?:0.0
        
        
        
        setContent {
            //val dataStore = StoreLanguage
            val savedLan = dataStore.languageFlow.collectAsState().value
            updateAppLang(this,savedLan)
            val currentLocale = LocalConfiguration.current.locale
            val layoutDirection = if (currentLocale.language == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(LocalLayoutDirection.provides(layoutDirection)) {

            WeatherForecastTheme {
                    navHostController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {

                    NavGraph(navController = navHostController)

            }}
            }
        }
    }
}


 fun updateAppLang(context: Context, locale: Locale){
     val selectedLanguage  = (locale)
     val resources = context.resources
     val configuration = resources.configuration
     if (selectedLanguage != configuration.locale) {
         val newConfig = Configuration(configuration)
         newConfig.setLocale(selectedLanguage)
         Locale.setDefault(selectedLanguage)
         resources.updateConfiguration(newConfig, null)
         //context.createConfigurationContext(newConfig)

     }

 }


