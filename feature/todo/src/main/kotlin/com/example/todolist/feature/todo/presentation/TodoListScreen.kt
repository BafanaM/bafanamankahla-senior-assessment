package com.example.todolist.feature.todo.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolist.core.ui.components.FullScreenLoading

@Composable
fun TodoListScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = hiltViewModel(),
    header: (@Composable () -> Unit)? = null,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.errorMessages.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        if (uiState.isLoading) {
            FullScreenLoading(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                contentPadding = PaddingValues(bottom = 96.dp),
            ) {
                header?.let { headerContent ->
                    item(key = "weather-header") { headerContent() }
                }

                if (uiState.todoTasks.isEmpty() && uiState.completedTasks.isEmpty()) {
                    item(key = "empty-state") { EmptyTasksMessage() }
                }

                if (uiState.todoTasks.isNotEmpty()) {
                    item(key = "todo-header") { SectionHeader(title = "To do", count = uiState.todoTasks.size) }
                    items(uiState.todoTasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onToggleCompleted = { viewModel.toggleCompleted(task) },
                            onDelete = { viewModel.deleteTask(task) },
                        )
                    }
                }

                if (uiState.completedTasks.isNotEmpty()) {
                    item(key = "completed-header") {
                        SectionHeader(title = "Completed", count = uiState.completedTasks.size)
                    }
                    items(uiState.completedTasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onToggleCompleted = { viewModel.toggleCompleted(task) },
                            onDelete = { viewModel.deleteTask(task) },
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, description ->
                viewModel.addTask(title, description)
                showAddDialog = false
            },
        )
    }
}

@Composable
private fun SectionHeader(title: String, count: Int, modifier: Modifier = Modifier) {
    Text(
        text = "$title ($count)",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Composable
private fun EmptyTasksMessage(modifier: Modifier = Modifier) {
    Text(
        text = "No tasks yet. Tap + to add your first task.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
    )
}
