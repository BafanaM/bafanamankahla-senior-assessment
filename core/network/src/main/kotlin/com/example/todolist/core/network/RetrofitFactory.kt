package com.example.todolist.core.network

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Generic networking layer: builds a ready-to-use [OkHttpClient] / [Retrofit] pair for any
 * base URL and JSON API, so each feature only has to declare its own Retrofit service interface.
 */
object RetrofitFactory {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    fun createOkHttpClient(debug: Boolean, interceptors: List<Interceptor> = emptyList()): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .apply {
                interceptors.forEach { addInterceptor(it) }
                if (debug) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        },
                    )
                }
            }
            .build()
    }

    fun create(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    fun <T> createService(retrofit: Retrofit, serviceClass: Class<T>): T =
        retrofit.create(serviceClass)
}
