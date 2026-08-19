package com.example.todolist.feature.weather.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

/** Best-effort mapping from WeatherAPI's free-text condition to a representative icon. */
fun weatherConditionIcon(conditionText: String): ImageVector {
    val text = conditionText.lowercase()
    return when {
        "thunder" in text -> Icons.Default.Bolt
        "snow" in text || "sleet" in text || "ice" in text || "blizzard" in text -> Icons.Default.AcUnit
        "rain" in text || "drizzle" in text || "shower" in text -> Icons.Default.Grain
        "fog" in text || "mist" in text || "haze" in text -> Icons.Default.BlurOn
        "cloud" in text || "overcast" in text -> Icons.Default.Cloud
        else -> Icons.Default.WbSunny
    }
}
