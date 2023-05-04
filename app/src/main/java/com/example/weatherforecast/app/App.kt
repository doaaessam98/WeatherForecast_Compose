package com.example.weatherforecast.app

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class App:Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
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

}

