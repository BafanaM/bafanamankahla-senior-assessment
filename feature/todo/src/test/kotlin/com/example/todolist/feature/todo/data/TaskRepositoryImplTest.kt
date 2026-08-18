package com.example.todolist.feature.todo.data

import com.example.todolist.core.common.DispatcherProvider
import com.example.todolist.core.database.TaskDao
import com.example.todolist.core.database.TaskEntity
import com.example.todolist.feature.todo.domain.model.Task
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TaskRepositoryImplTest {

    private val taskDao: TaskDao = mockk()
    private val dispatcherProvider: DispatcherProvider = mockk()
    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setUp() {
        every { dispatcherProvider.io } returns Dispatchers.Unconfined
        repository = TaskRepositoryImpl(taskDao, dispatcherProvider)
    }

    @Test
    fun `observeTasks maps entities to domain models`() = runTest {
        every { taskDao.observeTasks() } returns flowOf(
            listOf(TaskEntity(id = 1, title = "Buy milk", description = "2%", isCompleted = false, createdAt = 100L)),
        )

        val result = repository.observeTasks()

        result.collect { tasks ->
            assertThat(tasks).containsExactly(
                Task(id = 1, title = "Buy milk", description = "2%", isCompleted = false, createdAt = 100L),
            )
        }
    }

    @Test
    fun `setCompleted persists the updated completion flag`() = runTest {
        val entitySlot = slot<TaskEntity>()
        coEvery { taskDao.update(capture(entitySlot)) } returns Unit
        val task = Task(id = 1, title = "Buy milk", description = "", isCompleted = false)

        repository.setCompleted(task, true)

        assertThat(entitySlot.captured.isCompleted).isTrue()
        coVerify(exactly = 1) { taskDao.update(any()) }
    }
}
