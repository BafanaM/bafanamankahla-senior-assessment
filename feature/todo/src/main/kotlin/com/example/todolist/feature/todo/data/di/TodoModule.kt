package com.example.todolist.feature.todo.data.di

import com.example.todolist.feature.todo.data.TaskRepositoryImpl
import com.example.todolist.feature.todo.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodoModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}
