package com.example.todolist.feature.weather.presentation

import com.example.todolist.feature.weather.domain.model.WeatherInfo

sealed interface WeatherUiState {
    data class Loading(val isCustomLocation: Boolean = false) : WeatherUiState
    data class Success(val weather: WeatherInfo, val isCustomLocation: Boolean) : WeatherUiState
    data class Error(val message: String, val isCustomLocation: Boolean) : WeatherUiState
}
