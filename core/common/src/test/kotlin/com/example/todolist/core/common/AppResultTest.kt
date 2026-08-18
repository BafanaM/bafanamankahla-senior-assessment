package com.example.todolist.core.common

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AppResultTest {

    @Test
    fun `map transforms success data`() {
        val result: AppResult<Int> = AppResult.Success(2)

        val mapped = result.map { it * 10 }

        assertThat(mapped).isEqualTo(AppResult.Success(20))
    }

    @Test
    fun `map leaves error untouched`() {
        val result: AppResult<Int> = AppResult.Error("boom")

        val mapped = result.map { it * 10 }

        assertThat(mapped).isEqualTo(AppResult.Error("boom"))
    }
}
