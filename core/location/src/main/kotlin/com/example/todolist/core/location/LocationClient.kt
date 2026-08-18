package com.example.todolist.core.location

data class LocationData(
    val latitude: Double,
    val longitude: Double,
)

/**
 * Abstraction over the device's location provider. Returns `null` when location can't be
 * resolved (missing permission, location services disabled, or no fix available) so callers
 * can decide how to fall back (e.g. a default city) instead of crashing.
 */
interface LocationClient {
    suspend fun getCurrentLocation(): LocationData?
}
