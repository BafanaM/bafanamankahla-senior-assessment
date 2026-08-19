package com.example.todolist.di

import com.example.todolist.BuildConfig
import com.example.todolist.feature.weather.data.di.WeatherApiKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Supplies `:feature:weather` with app-specific configuration (the WeatherAPI key, sourced from
 * `local.properties`/`BuildConfig`). Kept separate from [AppModule] so this module's only reason
 * to change is weather configuration, not app-wide bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
object WeatherConfigModule {

    @Provides
    @WeatherApiKey
    fun provideWeatherApiKey(): String = BuildConfig.WEATHER_API_KEY
}
