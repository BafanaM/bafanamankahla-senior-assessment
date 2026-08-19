package com.example.todolist.feature.weather.domain.repository

import com.example.todolist.core.common.AppResult
import com.example.todolist.feature.weather.domain.model.WeatherInfo

interface WeatherRepository {
    /**
     * [locationQuery] is passed straight through to WeatherAPI's flexible `q` parameter, so it
     * accepts either `"lat,lon"` coordinates or a free-text place name (city, zip/postal code).
     */
    suspend fun getWeather(locationQuery: String): AppResult<WeatherInfo>
}
