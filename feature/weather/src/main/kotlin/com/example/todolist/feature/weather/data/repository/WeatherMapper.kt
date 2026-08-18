package com.example.todolist.feature.weather.data.repository

import com.example.todolist.feature.weather.data.remote.WeatherForecastResponseDto
import com.example.todolist.feature.weather.domain.model.WeatherInfo

fun WeatherForecastResponseDto.toDomain(): WeatherInfo {
    val astro = forecast.forecastday.firstOrNull()?.astro
    return WeatherInfo(
        locationName = location.name,
        temperatureCelsius = current.tempC,
        conditionText = current.condition.text,
        sunrise = astro?.sunrise ?: "--",
        sunset = astro?.sunset ?: "--",
    )
}
