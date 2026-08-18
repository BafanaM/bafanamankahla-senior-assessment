package com.example.todolist.core.common

/**
 * Generic wrapper for operations that can fail, used across layers so the UI
 * never has to deal with raw exceptions.
 */
sealed class AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>()
    data class Error(val message: String, val cause: Throwable? = null) : AppResult<Nothing>()
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Error -> this
}
