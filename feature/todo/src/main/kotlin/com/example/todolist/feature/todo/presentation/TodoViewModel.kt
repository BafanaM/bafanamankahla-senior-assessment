package com.example.todolist.feature.todo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.core.common.AppResult
import com.example.todolist.feature.todo.domain.model.Task
import com.example.todolist.feature.todo.domain.usecase.AddTaskUseCase
import com.example.todolist.feature.todo.domain.usecase.DeleteTaskUseCase
import com.example.todolist.feature.todo.domain.usecase.ObserveTasksUseCase
import com.example.todolist.feature.todo.domain.usecase.ToggleTaskCompletionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val observeTasksUseCase: ObserveTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val toggleTaskCompletionUseCase: ToggleTaskCompletionUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    private val _errorMessages = MutableSharedFlow<String>()
    val errorMessages = _errorMessages.asSharedFlow()

    init {
        observeTasksUseCase()
            .onEach { tasks ->
                _uiState.update { current ->
                    current.copy(
                        todoTasks = tasks.filterNot { it.isCompleted },
                        completedTasks = tasks.filter { it.isCompleted },
                        isLoading = false,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            when (val result = addTaskUseCase(title, description)) {
                is AppResult.Error -> _errorMessages.emit(result.message)
                is AppResult.Success -> Unit
            }
        }
    }

    fun toggleCompleted(task: Task) {
        viewModelScope.launch { toggleTaskCompletionUseCase(task) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { deleteTaskUseCase(task) }
    }
}
