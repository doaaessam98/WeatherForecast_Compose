package com.example.weatherforecast.data.repoImp.weather

import android.content.ContentValues.TAG
import android.util.Log
import com.example.weatherforecast.app.Utils.DataResult
import com.example.weatherforecast.data.source.local.LocalDataSource
import com.example.weatherforecast.data.source.remote.RemoteDataSource
import com.example.weatherforecast.domain.models.db.Weather
import com.example.weatherforecast.domain.models.db.WeatherDB
import com.example.weatherforecast.domain.models.db.toWeatherModel
import com.example.weatherforecast.domain.repo.WeatherRepo
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepoImp @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : WeatherRepo {

   override  suspend fun getWeatherData(latitude: Double, longitude: Double):DataResult<Weather>{
       refreshWeatherData(latitude,longitude)
       return try {
                 val result  =  localDataSource.getWeatherData(latitude,longitude)
                  DataResult.Success(result.toWeatherModel())

              }catch(e:Exception){
                DataResult.Error(e.localizedMessage)
             }
         }




    override suspend fun refreshWeatherData(lat: Double, lon: Double) {
        withContext(Dispatchers.IO){
            try {
                    val response = remoteDataSource.getWeatherData(lat, lon)

                    localDataSource.insertWeatherData(response.toWeatherDataBase())


            }catch (e:Exception){
                Log.e(TAG, "refreshWeatherData:Error ${e.localizedMessage}", )
            }
    }
    }

    override suspend fun insertDataInDataBase(latLng: LatLng) {
        Log.e(TAG, "insertDataInDataBase: ", )
        refreshWeatherData(latLng.latitude,latLng.longitude)
    }

    override suspend fun getCities(): Flow<List<WeatherDB>> =localDataSource.getCities()
    override suspend fun deleteCities(cities: Set<String>)  = localDataSource.deleteCities(cities)
}