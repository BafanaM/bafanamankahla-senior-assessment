package com.example.todolist.feature.weather.presentation

import com.example.todolist.feature.weather.domain.model.WeatherInfo

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val weather: WeatherInfo) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}
