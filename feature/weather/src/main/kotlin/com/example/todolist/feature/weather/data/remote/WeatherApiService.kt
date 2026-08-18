package com.example.todolist.feature.weather.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * https://www.weatherapi.com/docs/ - forecast endpoint is used (instead of /current.json)
 * because it's the only one that also returns astro data (sunrise/sunset) for the day.
 */
interface WeatherApiService {

    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("days") days: Int = 1,
        @Query("aqi") airQuality: String = "no",
        @Query("alerts") alerts: String = "no",
    ): WeatherForecastResponseDto

    companion object {
        const val BASE_URL = "https://api.weatherapi.com/"
    }
}
