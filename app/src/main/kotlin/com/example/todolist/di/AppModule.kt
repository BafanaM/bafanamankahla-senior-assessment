package com.example.todolist.di

import com.example.todolist.BuildConfig
import com.example.todolist.core.common.DefaultDispatcherProvider
import com.example.todolist.core.common.DispatcherProvider
import com.example.todolist.feature.weather.data.di.WeatherApiKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @WeatherApiKey
    fun provideWeatherApiKey(): String = BuildConfig.WEATHER_API_KEY
}
