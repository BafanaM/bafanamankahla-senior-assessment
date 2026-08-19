package com.example.todolist.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.feature.weather.presentation.WeatherViewModel

/**
 * Owns both the "My Tasks" title and the location-search entry point for the weather card.
 * Lives in `:app` (rather than `:feature:todo`) because it needs [WeatherViewModel], which
 * belongs to `:feature:weather` - `:feature:todo` stays unaware of weather entirely. Search and
 * the weather card below share the same Activity-scoped `WeatherViewModel` instance, so
 * triggering a search here is immediately reflected there.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = hiltViewModel(),
) {
    var searchExpanded by rememberSaveable { mutableStateOf(false) }

    if (searchExpanded) {
        var query by rememberSaveable { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        TopAppBar(
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = { searchExpanded = false }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close search")
                }
            },
            title = {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    placeholder = { Text("Search city, zip, or airport code") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (query.isNotBlank()) {
                                weatherViewModel.search(query)
                                searchExpanded = false
                            }
                        },
                    ),
                )
            },
            actions = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear search")
                    }
                }
            },
            scrollBehavior = scrollBehavior,
        )
        return
    }

    TopAppBar(
        modifier = modifier,
        title = { Text("My Tasks") },
        actions = {
            IconButton(onClick = { searchExpanded = true }) {
                Icon(Icons.Default.Search, contentDescription = "Search for a location")
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
