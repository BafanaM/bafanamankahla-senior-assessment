package com.example.todolist.feature.weather.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolist.feature.weather.domain.model.WeatherInfo
import kotlin.math.roundToInt

/**
 * Renders the weather result for whatever the shared [WeatherViewModel] currently has loaded.
 * The search UI that drives [viewModel] lives in the app's top bar (see `MainTopBar`) - since
 * both are backed by the same Activity-scoped `hiltViewModel()` instance, they stay in sync.
 */
@Composable
fun WeatherHeader(
    hasLocationPermission: Boolean,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) viewModel.useCurrentLocation()
    }

    val isCustomLocation = when (val state = uiState) {
        is WeatherUiState.Loading -> state.isCustomLocation
        is WeatherUiState.Success -> state.isCustomLocation
        is WeatherUiState.Error -> state.isCustomLocation
    }
    val needsPermissionPrompt = !hasLocationPermission &&
        !isCustomLocation &&
        uiState is WeatherUiState.Error

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when {
                needsPermissionPrompt -> PermissionNeeded(onRequestPermission)
                uiState is WeatherUiState.Loading -> WeatherLoading()
                uiState is WeatherUiState.Error -> WeatherError(
                    message = (uiState as WeatherUiState.Error).message,
                    onRetry = { viewModel.retry() },
                )
                uiState is WeatherUiState.Success -> {
                    val success = uiState as WeatherUiState.Success
                    WeatherContent(success.weather)
                    if (success.isCustomLocation && hasLocationPermission) {
                        TextButton(onClick = { viewModel.useCurrentLocation() }) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text("Use current location")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionNeeded(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = "Allow location access to see the weather for where you are, or search for a place using the search icon above.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
        )
        TextButton(onClick = onRequestPermission) {
            Text("Grant location access")
        }
    }
}

@Composable
private fun WeatherLoading() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = "Fetching weather…",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun WeatherError(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
        )
        TextButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun WeatherContent(weather: WeatherInfo) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = weather.locationName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                modifier = Modifier.size(64.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = weatherConditionIcon(weather.conditionText),
                        contentDescription = null,
                        modifier = Modifier.size(34.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(
                    text = "${weather.temperatureCelsius.roundToInt()}°C",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = weather.conditionText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
            ) {
                SunTimeInfo(
                    icon = Icons.Default.WbTwilight,
                    label = "Sunrise",
                    time = weather.sunrise,
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 24.dp),
                )
                SunTimeInfo(
                    icon = Icons.Default.NightsStay,
                    label = "Sunset",
                    time = weather.sunset,
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 24.dp),
                )
            }
        }
    }
}

@Composable
private fun SunTimeInfo(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    time: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = time,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}
