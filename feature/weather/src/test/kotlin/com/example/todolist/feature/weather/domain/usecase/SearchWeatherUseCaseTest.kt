package com.example.todolist.feature.weather.domain.usecase

import com.example.todolist.core.common.AppResult
import com.example.todolist.feature.weather.domain.model.WeatherInfo
import com.example.todolist.feature.weather.domain.repository.WeatherRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchWeatherUseCaseTest {

    private val weatherRepository: WeatherRepository = mockk()
    private lateinit var useCase: SearchWeatherUseCase

    @Before
    fun setUp() {
        useCase = SearchWeatherUseCase(weatherRepository)
    }

    @Test
    fun `blank query returns error and does not call repository`() = runTest {
        val result = useCase("   ")

        assertThat(result).isInstanceOf(AppResult.Error::class.java)
        coVerify(exactly = 0) { weatherRepository.getWeather(any()) }
    }

    @Test
    fun `trims query and delegates to repository`() = runTest {
        val weather = WeatherInfo(
            locationName = "Paris",
            temperatureCelsius = 15.0,
            conditionText = "Cloudy",
            sunrise = "07:00 AM",
            sunset = "08:00 PM",
        )
        coEvery { weatherRepository.getWeather("Paris") } returns AppResult.Success(weather)

        val result = useCase("  Paris  ")

        assertThat(result).isEqualTo(AppResult.Success(weather))
        coVerify(exactly = 1) { weatherRepository.getWeather("Paris") }
    }
}
