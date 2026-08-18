package com.example.todolist.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.todolist.feature.todo.presentation.TodoListScreen
import com.example.todolist.feature.weather.presentation.WeatherHeader

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(context.hasLocationPermission()) }
    var hasRequestedOnLaunch by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { results ->
        hasLocationPermission = results.values.any { granted -> granted }
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission && !hasRequestedOnLaunch) {
            hasRequestedOnLaunch = true
            permissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            )
        }
    }

    TodoListScreen(
        modifier = modifier,
        header = {
            WeatherHeader(
                hasLocationPermission = hasLocationPermission,
                onRequestPermission = {
                    permissionLauncher.launch(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    )
                },
            )
        },
    )
}

private fun Context.hasLocationPermission(): Boolean {
    val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
    return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
}
