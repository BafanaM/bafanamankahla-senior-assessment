package com.example.todolist.feature.weather.domain.repository

import com.example.todolist.core.common.AppResult
import com.example.todolist.feature.weather.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): AppResult<WeatherInfo>
}
