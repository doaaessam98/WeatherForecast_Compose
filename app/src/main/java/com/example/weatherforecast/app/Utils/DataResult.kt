package com.example.weatherforecast.app.Utils

sealed class DataResult<out T : Any> {
        data class Success<out T : Any>(val data: T) : DataResult<T>()
        data class Error(val message: String?, val statusCode: Int? = null) :
            DataResult<Nothing>()
       object  Loading : DataResult<Nothing>()
    }
