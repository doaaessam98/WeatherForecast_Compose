package com.example.weatherforecast.data.source.local.datastore


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*

class StoreLanguage(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("APP_LANGUAGE")
        val APP_LANGUAGE_KEY = stringPreferencesKey("app_lan")
    }

    val languageFlow: StateFlow<Locale> = context.dataStore.data
        .map { preferences ->
            val languageCode = preferences[APP_LANGUAGE_KEY] ?: Locale.getDefault().language
            Locale(languageCode)
        }
        .stateIn(CoroutineScope(Dispatchers.Main),SharingStarted.Eagerly, Locale.getDefault())



    suspend fun saveLanguage(name: String) {
        context.dataStore.edit { preferences ->
            preferences[APP_LANGUAGE_KEY] = name
        }
    }
}