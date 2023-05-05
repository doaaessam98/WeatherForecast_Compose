package com.example.weatherforecast.app.di

import android.content.Context
import com.example.weatherforecast.data.repoImp.alarm.AlarmRepoImp
import com.example.weatherforecast.data.repoImp.weather.WeatherRepoImp
import com.example.weatherforecast.data.source.local.LocalDataSource
import com.example.weatherforecast.data.source.local.LocalDataSourceImp
import com.example.weatherforecast.data.source.local.datastore.StoreLanguage
import com.example.weatherforecast.data.source.remote.RemoteDataSource
import com.example.weatherforecast.data.source.remote.RemoteDataSourceImp
import com.example.weatherforecast.domain.repo.AlarmRepo
import com.example.weatherforecast.domain.repo.WeatherRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
interface AppModule {

    @Binds
    fun provideLocalDataSource(localDataSource: LocalDataSourceImp): LocalDataSource

    @Binds
    fun provideRemoteDataSource(remoteDataSource: RemoteDataSourceImp): RemoteDataSource

    @Binds
    fun provideRepository(repository: WeatherRepoImp): WeatherRepo

    @Binds
    fun provideAlarmRepository(repository: AlarmRepoImp): AlarmRepo


}