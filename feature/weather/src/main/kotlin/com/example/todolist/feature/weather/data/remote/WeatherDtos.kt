package com.example.todolist.feature.weather.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherForecastResponseDto(
    val location: LocationDto,
    val current: CurrentWeatherDto,
    val forecast: ForecastDto,
)

@Serializable
data class LocationDto(
    val name: String,
)

@Serializable
data class CurrentWeatherDto(
    @SerialName("temp_c") val tempC: Double,
    val condition: ConditionDto,
)

@Serializable
data class ConditionDto(
    val text: String,
)

@Serializable
data class ForecastDto(
    val forecastday: List<ForecastDayDto>,
)

@Serializable
data class ForecastDayDto(
    val astro: AstroDto,
)

@Serializable
data class AstroDto(
    val sunrise: String,
    val sunset: String,
)
