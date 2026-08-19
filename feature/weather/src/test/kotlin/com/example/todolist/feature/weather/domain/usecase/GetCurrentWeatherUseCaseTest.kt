package com.example.todolist.feature.weather.domain.usecase

import com.example.todolist.core.common.AppResult
import com.example.todolist.core.location.LocationClient
import com.example.todolist.core.location.LocationData
import com.example.todolist.feature.weather.domain.model.WeatherInfo
import com.example.todolist.feature.weather.domain.repository.WeatherRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCurrentWeatherUseCaseTest {

    private val locationClient: LocationClient = mockk()
    private val weatherRepository: WeatherRepository = mockk()
    private lateinit var useCase: GetCurrentWeatherUseCase

    @Before
    fun setUp() {
        useCase = GetCurrentWeatherUseCase(locationClient, weatherRepository)
    }

    @Test
    fun `returns error when location cannot be resolved`() = runTest {
        coEvery { locationClient.getCurrentLocation() } returns null

        val result = useCase()

        assertThat(result).isInstanceOf(AppResult.Error::class.java)
        coVerify(exactly = 0) { weatherRepository.getWeather(any()) }
    }

    @Test
    fun `delegates to repository using the resolved coordinates`() = runTest {
        val location = LocationData(latitude = -33.9, longitude = 18.4)
        val weather = WeatherInfo(
            locationName = "Cape Town",
            temperatureCelsius = 20.0,
            conditionText = "Clear",
            sunrise = "06:00 AM",
            sunset = "06:00 PM",
        )
        coEvery { locationClient.getCurrentLocation() } returns location
        coEvery { weatherRepository.getWeather("-33.9,18.4") } returns AppResult.Success(weather)

        val result = useCase()

        assertThat(result).isEqualTo(AppResult.Success(weather))
    }
}
