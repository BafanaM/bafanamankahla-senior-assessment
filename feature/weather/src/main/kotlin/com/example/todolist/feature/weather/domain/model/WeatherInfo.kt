package com.example.todolist.feature.weather.domain.model

data class WeatherInfo(
    val locationName: String,
    val temperatureCelsius: Double,
    val conditionText: String,
    val sunrise: String,
    val sunset: String,
)
