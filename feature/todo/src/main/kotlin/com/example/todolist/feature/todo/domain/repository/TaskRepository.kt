package com.example.todolist.feature.todo.domain.repository

import com.example.todolist.feature.todo.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(): Flow<List<Task>>
    suspend fun addTask(title: String, description: String)
    suspend fun setCompleted(task: Task, isCompleted: Boolean)
    suspend fun deleteTask(task: Task)
}
