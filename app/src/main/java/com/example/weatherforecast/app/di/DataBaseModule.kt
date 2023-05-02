package com.example.weatherforecast.app.di

import android.content.Context
import androidx.room.Room
import com.example.weatherforecast.data.source.local.db.AlarmDao
import com.example.weatherforecast.data.source.local.db.WeatherDao
import com.example.weatherforecast.data.source.local.db.WeatherDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {

    @Provides
    @Singleton
    fun weatherDataBase(@ApplicationContext context: Context): WeatherDataBase =
        Room.databaseBuilder(context, WeatherDataBase::class.java,"weather_DB").

        fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideWeatherDataBase(db:WeatherDataBase ): WeatherDao =db.weatherDao()

    @Provides
    @Singleton
    fun provideAlarmDao(db:WeatherDataBase ): AlarmDao =db.alarmDao()

}