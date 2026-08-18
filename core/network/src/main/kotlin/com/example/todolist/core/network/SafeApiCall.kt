package com.example.todolist.core.network

import com.example.todolist.core.common.AppResult
import retrofit2.HttpException
import java.io.IOException

/**
 * Executes [apiCall] and converts any failure into an [AppResult.Error], so callers
 * (repositories) never have to deal with raw network exceptions directly.
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): AppResult<T> {
    return try {
        AppResult.Success(apiCall())
    } catch (e: HttpException) {
        AppResult.Error(
            message = "Server error (${e.code()}): ${e.message()}",
            cause = e,
        )
    } catch (e: IOException) {
        AppResult.Error(
            message = "No internet connection. Please check your network and try again.",
            cause = e,
        )
    } catch (e: Exception) {
        AppResult.Error(
            message = e.message ?: "Something went wrong",
            cause = e,
        )
    }
}
