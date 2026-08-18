package com.example.todolist.feature.todo.data

import com.example.todolist.core.common.DispatcherProvider
import com.example.todolist.core.database.TaskDao
import com.example.todolist.feature.todo.domain.model.Task
import com.example.todolist.feature.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val dispatcherProvider: DispatcherProvider,
) : TaskRepository {

    override fun observeTasks(): Flow<List<Task>> =
        taskDao.observeTasks().map { entities -> entities.map { it.toDomain() } }

    override suspend fun addTask(title: String, description: String) {
        withContext(dispatcherProvider.io) {
            taskDao.upsert(Task(title = title, description = description).toEntity())
        }
    }

    override suspend fun setCompleted(task: Task, isCompleted: Boolean) {
        withContext(dispatcherProvider.io) {
            taskDao.update(task.copy(isCompleted = isCompleted).toEntity())
        }
    }

    override suspend fun deleteTask(task: Task) {
        withContext(dispatcherProvider.io) {
            taskDao.delete(task.toEntity())
        }
    }
}
