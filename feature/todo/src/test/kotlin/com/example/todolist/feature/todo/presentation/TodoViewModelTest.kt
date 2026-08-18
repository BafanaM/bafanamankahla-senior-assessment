package com.example.todolist.feature.todo.presentation

import app.cash.turbine.test
import com.example.todolist.core.common.AppResult
import com.example.todolist.feature.todo.MainDispatcherRule
import com.example.todolist.feature.todo.domain.model.Task
import com.example.todolist.feature.todo.domain.usecase.AddTaskUseCase
import com.example.todolist.feature.todo.domain.usecase.DeleteTaskUseCase
import com.example.todolist.feature.todo.domain.usecase.ObserveTasksUseCase
import com.example.todolist.feature.todo.domain.usecase.ToggleTaskCompletionUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TodoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val tasksFlow = MutableSharedFlow<List<Task>>(replay = 1)
    private val observeTasksUseCase: ObserveTasksUseCase = mockk()
    private val addTaskUseCase: AddTaskUseCase = mockk()
    private val toggleTaskCompletionUseCase: ToggleTaskCompletionUseCase = mockk(relaxed = true)
    private val deleteTaskUseCase: DeleteTaskUseCase = mockk(relaxed = true)

    private lateinit var viewModel: TodoViewModel

    private val incompleteTask = Task(id = 1, title = "Buy milk", description = "", isCompleted = false)
    private val completedTask = Task(id = 2, title = "Walk the dog", description = "", isCompleted = true)

    @Before
    fun setUp() {
        every { observeTasksUseCase() } returns tasksFlow
        viewModel = TodoViewModel(observeTasksUseCase, addTaskUseCase, toggleTaskCompletionUseCase, deleteTaskUseCase)
    }

    @Test
    fun `splits emitted tasks into todo and completed sections`() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem().isLoading).isTrue()

            tasksFlow.emit(listOf(incompleteTask, completedTask))

            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.todoTasks).containsExactly(incompleteTask)
            assertThat(state.completedTasks).containsExactly(completedTask)
        }
    }

    @Test
    fun `addTask surfaces the use case error message`() = runTest {
        coEvery { addTaskUseCase(any(), any()) } returns AppResult.Error("Title can't be empty")

        viewModel.errorMessages.test {
            viewModel.addTask("", "description")
            assertThat(awaitItem()).isEqualTo("Title can't be empty")
        }
    }
}
