package com.example.todolist.feature.weather.data.di

import com.example.todolist.core.network.RetrofitFactory
import com.example.todolist.feature.weather.BuildConfig
import com.example.todolist.feature.weather.data.remote.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        RetrofitFactory.createOkHttpClient(debug = BuildConfig.DEBUG)

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        RetrofitFactory.create(WeatherApiService.BASE_URL, okHttpClient)

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService =
        RetrofitFactory.createService(retrofit, WeatherApiService::class.java)
}
