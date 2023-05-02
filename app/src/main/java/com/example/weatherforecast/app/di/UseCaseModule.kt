package com.example.weatherforecast.app.di

import android.content.ContentValues.TAG
import android.util.Log
import com.example.weatherforecast.data.repoImp.weather.WeatherRepoImp
import com.example.weatherforecast.domain.repo.WeatherRepo
import com.example.weatherforecast.domain.useCase.GetWeatherDataUseCase
import com.google.android.gms.location.Priority
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {



    @Provides
    @Singleton
    fun provideGetWeatherDataUseCase(repository: WeatherRepo): GetWeatherDataUseCase{
        return GetWeatherDataUseCase(repository)
    }

}