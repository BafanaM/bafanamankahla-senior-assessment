package com.example.todolist.feature.todo.domain.usecase

import com.example.todolist.core.common.AppResult
import com.example.todolist.feature.todo.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke(title: String, description: String): AppResult<Unit> {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isEmpty()) {
            return AppResult.Error("Title can't be empty")
        }
        repository.addTask(trimmedTitle, description.trim())
        return AppResult.Success(Unit)
    }
}
