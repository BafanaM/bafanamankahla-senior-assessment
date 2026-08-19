package com.example.todolist.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Read-only snapshot of the location-permission flow, exposed to composables that need it
 * (`WeatherHeader`) without giving them access to how permissions are actually requested.
 */
@Immutable
data class LocationPermissionState(
    val hasPermission: Boolean,
    val requestPermission: () -> Unit,
)

private val LOCATION_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

/**
 * Owns the location-permission check and the system permission request flow. Kept separate from
 * [MainScreen] so each has a single reason to change: this only concerns itself with "do we have
 * permission, and how do we ask for it" - screen composition and layout live elsewhere.
 */
@Composable
fun rememberLocationPermissionState(requestOnFirstComposition: Boolean = true): LocationPermissionState {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(context.hasLocationPermission()) }
    var hasRequestedOnLaunch by rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { results ->
        hasPermission = results.values.any { granted -> granted }
    }

    LaunchedEffect(Unit) {
        if (requestOnFirstComposition && !hasPermission && !hasRequestedOnLaunch) {
            hasRequestedOnLaunch = true
            launcher.launch(LOCATION_PERMISSIONS)
        }
    }

    return remember(hasPermission) {
        LocationPermissionState(
            hasPermission = hasPermission,
            requestPermission = { launcher.launch(LOCATION_PERMISSIONS) },
        )
    }
}

private fun Context.hasLocationPermission(): Boolean {
    val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
    return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
}
