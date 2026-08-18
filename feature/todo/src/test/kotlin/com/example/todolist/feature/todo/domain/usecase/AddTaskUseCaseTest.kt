package com.example.todolist.feature.todo.domain.usecase

import com.example.todolist.core.common.AppResult
import com.example.todolist.feature.todo.domain.repository.TaskRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddTaskUseCaseTest {

    private val repository: TaskRepository = mockk(relaxed = true)
    private lateinit var useCase: AddTaskUseCase

    @Before
    fun setUp() {
        useCase = AddTaskUseCase(repository)
    }

    @Test
    fun `blank title returns error and does not persist`() = runTest {
        val result = useCase("   ", "some description")

        assertThat(result).isInstanceOf(AppResult.Error::class.java)
        coVerify(exactly = 0) { repository.addTask(any(), any()) }
    }

    @Test
    fun `valid title is trimmed and persisted`() = runTest {
        coEvery { repository.addTask(any(), any()) } returns Unit

        val result = useCase("  Buy milk  ", "  2 percent  ")

        assertThat(result).isEqualTo(AppResult.Success(Unit))
        coVerify(exactly = 1) { repository.addTask("Buy milk", "2 percent") }
    }
}
