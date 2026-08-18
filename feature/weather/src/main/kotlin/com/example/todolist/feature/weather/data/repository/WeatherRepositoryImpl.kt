package com.example.todolist.feature.weather.data.repository

import com.example.todolist.core.common.AppResult
import com.example.todolist.core.common.DispatcherProvider
import com.example.todolist.core.common.map
import com.example.todolist.core.network.safeApiCall
import com.example.todolist.feature.weather.data.di.WeatherApiKey
import com.example.todolist.feature.weather.data.remote.WeatherApiService
import com.example.todolist.feature.weather.domain.model.WeatherInfo
import com.example.todolist.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
    @WeatherApiKey private val apiKey: String,
    private val dispatcherProvider: DispatcherProvider,
) : WeatherRepository {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): AppResult<WeatherInfo> =
        withContext(dispatcherProvider.io) {
            if (apiKey.isBlank()) {
                return@withContext AppResult.Error(
                    "Missing WeatherAPI key. Add WEATHER_API_KEY to local.properties (see README).",
                )
            }
            safeApiCall {
                api.getForecast(apiKey = apiKey, query = "$latitude,$longitude")
            }.map { it.toDomain() }
        }
}
