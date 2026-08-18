package com.example.todolist.feature.todo.domain.usecase

import com.example.todolist.feature.todo.domain.model.Task
import com.example.todolist.feature.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTasksUseCase @Inject constructor(
    private val repository: TaskRepository,
) {
    operator fun invoke(): Flow<List<Task>> = repository.observeTasks()
}
