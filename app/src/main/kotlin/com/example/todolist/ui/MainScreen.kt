package com.example.todolist.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todolist.feature.todo.presentation.TodoListScreen
import com.example.todolist.feature.weather.presentation.WeatherHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val locationPermission = rememberLocationPermissionState()

    TodoListScreen(
        modifier = modifier,
        topBar = { scrollBehavior -> MainTopBar(scrollBehavior = scrollBehavior) },
        header = {
            WeatherHeader(
                hasLocationPermission = locationPermission.hasPermission,
                onRequestPermission = locationPermission.requestPermission,
            )
        },
    )
}
