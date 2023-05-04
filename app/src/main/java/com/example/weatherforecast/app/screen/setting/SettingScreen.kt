package com.example.weatherforecast.app.screen.setting


import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.os.ConfigurationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecast.R

import kotlinx.coroutines.launch
import java.util.*
import com.example.weatherforecast.data.source.local.datastore.StoreLanguage

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun  SettingScreen(
    modifier: Modifier=Modifier
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreLanguage(context)

    val selectedLanguage  = dataStore.languageFlow.collectAsState().value
    val languages = listOf(Locale("en", "US"), Locale("ar", "EG"))


                 Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.language),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    languages.forEach { language ->
                        LanguageOption(language = language, selectedLanguage = selectedLanguage, onLanguageSelected = {
                            Log.e(TAG, "SettingScreen:${language} ", )
                            Log.e(TAG, "SettingScreen2:${selectedLanguage} ", )

                            scope.launch {
                              dataStore.saveLanguage(it.language)
                            }

                        })
                    }
                }
            }

    @Composable
    fun LanguageOption(language: Locale, selectedLanguage: Locale, onLanguageSelected: (Locale) -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp)
                .selectable(
                    selected =  selectedLanguage.language == language.language,
                    onClick = { onLanguageSelected(language) })
        ) {
            RadioButton(
                selected = selectedLanguage.language == language.language,
                onClick = {
                    onLanguageSelected(language)


                }
            )
            Text(
                text = language.displayLanguage,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }




fun updateLocale(language: String,context: Context) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = Configuration().apply {
        setLocale(locale)
    }
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}



private fun getCurrentLocale(context:Context): Locale? {
    return ConfigurationCompat.getLocales(context.resources.configuration)[0]
}

private fun setCurrentLocale(context: Context, locale: Locale) {
    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    context.createConfigurationContext(config)
}



