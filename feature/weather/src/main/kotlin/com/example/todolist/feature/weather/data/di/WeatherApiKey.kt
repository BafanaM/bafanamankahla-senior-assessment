package com.example.todolist.feature.weather.data.di

import javax.inject.Qualifier

/**
 * The WeatherAPI.com key is app-specific configuration (it lives in `local.properties` /
 * `BuildConfig` on the `:app` module), so it's supplied to this feature module through a
 * qualified binding rather than this module reading `BuildConfig` itself.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherApiKey
