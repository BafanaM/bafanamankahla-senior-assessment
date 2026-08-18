package com.example.todolist.feature.todo.presentation

import com.example.todolist.feature.todo.domain.model.Task

data class TodoUiState(
    val todoTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
)
