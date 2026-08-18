package com.example.todolist.feature.weather.domain.usecase

import com.example.todolist.core.common.AppResult
import com.example.todolist.core.location.LocationClient
import com.example.todolist.feature.weather.domain.model.WeatherInfo
import com.example.todolist.feature.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val locationClient: LocationClient,
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(): AppResult<WeatherInfo> {
        val location = locationClient.getCurrentLocation()
            ?: return AppResult.Error(
                "Couldn't determine your location. Make sure location access is granted and location services are on.",
            )
        return weatherRepository.getCurrentWeather(location.latitude, location.longitude)
    }
}
