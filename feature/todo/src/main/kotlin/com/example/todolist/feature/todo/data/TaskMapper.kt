package com.example.todolist.feature.todo.data

import com.example.todolist.core.database.TaskEntity
import com.example.todolist.feature.todo.domain.model.Task

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt,
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt,
)
