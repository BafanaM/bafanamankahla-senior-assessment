package com.example.todolist.feature.todo.domain.usecase

import com.example.todolist.feature.todo.domain.model.Task
import com.example.todolist.feature.todo.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke(task: Task) {
        repository.deleteTask(task)
    }
}
