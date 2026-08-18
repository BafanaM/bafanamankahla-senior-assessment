package com.example.todolist.feature.weather.data.repository

import com.example.todolist.feature.weather.data.remote.AstroDto
import com.example.todolist.feature.weather.data.remote.ConditionDto
import com.example.todolist.feature.weather.data.remote.CurrentWeatherDto
import com.example.todolist.feature.weather.data.remote.ForecastDayDto
import com.example.todolist.feature.weather.data.remote.ForecastDto
import com.example.todolist.feature.weather.data.remote.LocationDto
import com.example.todolist.feature.weather.data.remote.WeatherForecastResponseDto
import com.example.todolist.feature.weather.domain.model.WeatherInfo
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class WeatherMapperTest {

    @Test
    fun `maps dto to domain model`() {
        val dto = WeatherForecastResponseDto(
            location = LocationDto(name = "Cape Town"),
            current = CurrentWeatherDto(tempC = 18.5, condition = ConditionDto(text = "Sunny")),
            forecast = ForecastDto(
                forecastday = listOf(ForecastDayDto(astro = AstroDto(sunrise = "06:30 AM", sunset = "06:00 PM"))),
            ),
        )

        val result = dto.toDomain()

        assertThat(result).isEqualTo(
            WeatherInfo(
                locationName = "Cape Town",
                temperatureCelsius = 18.5,
                conditionText = "Sunny",
                sunrise = "06:30 AM",
                sunset = "06:00 PM",
            ),
        )
    }

    @Test
    fun `falls back to placeholder sun times when forecast day is missing`() {
        val dto = WeatherForecastResponseDto(
            location = LocationDto(name = "Cape Town"),
            current = CurrentWeatherDto(tempC = 18.5, condition = ConditionDto(text = "Sunny")),
            forecast = ForecastDto(forecastday = emptyList()),
        )

        val result = dto.toDomain()

        assertThat(result.sunrise).isEqualTo("--")
        assertThat(result.sunset).isEqualTo("--")
    }
}
