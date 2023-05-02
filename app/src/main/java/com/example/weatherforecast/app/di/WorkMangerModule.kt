package com.example.weatherforecast.app.di

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.Initializer
import androidx.work.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton






@InstallIn(SingletonComponent::class)
@Module
object WorkMangerModule {


    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
       return WorkManager.getInstance(context)
    }



}