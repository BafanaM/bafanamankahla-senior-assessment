package com.example.todolist.feature.weather.domain.usecase

import com.example.todolist.core.common.AppResult
import com.example.todolist.feature.weather.domain.model.WeatherInfo
import com.example.todolist.feature.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class SearchWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(locationQuery: String): AppResult<WeatherInfo> {
        val trimmed = locationQuery.trim()
        if (trimmed.isEmpty()) {
            return AppResult.Error("Enter a place name to search")
        }
        return weatherRepository.getWeather(trimmed)
    }
}
