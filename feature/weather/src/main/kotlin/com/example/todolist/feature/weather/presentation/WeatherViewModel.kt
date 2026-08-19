package com.example.todolist.feature.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.core.common.AppResult
import com.example.todolist.feature.weather.domain.usecase.GetCurrentWeatherUseCase
import com.example.todolist.feature.weather.domain.usecase.SearchWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val searchWeatherUseCase: SearchWeatherUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var lastSearchQuery: String? = null

    init {
        useCurrentLocation()
    }

    /** Re-resolves the device's current location and fetches weather for it. */
    fun useCurrentLocation() {
        lastSearchQuery = null
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading(isCustomLocation = false)
            _uiState.value = when (val result = getCurrentWeatherUseCase()) {
                is AppResult.Success -> WeatherUiState.Success(result.data, isCustomLocation = false)
                is AppResult.Error -> WeatherUiState.Error(result.message, isCustomLocation = false)
            }
        }
    }

    /** Fetches weather for a user-searched place name instead of the device's location. */
    fun search(locationQuery: String) {
        lastSearchQuery = locationQuery
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading(isCustomLocation = true)
            _uiState.value = when (val result = searchWeatherUseCase(locationQuery)) {
                is AppResult.Success -> WeatherUiState.Success(result.data, isCustomLocation = true)
                is AppResult.Error -> WeatherUiState.Error(result.message, isCustomLocation = true)
            }
        }
    }

    /** Retries whichever mode (current location vs. searched place) was last active. */
    fun retry() {
        val query = lastSearchQuery
        if (query != null) search(query) else useCurrentLocation()
    }
}
