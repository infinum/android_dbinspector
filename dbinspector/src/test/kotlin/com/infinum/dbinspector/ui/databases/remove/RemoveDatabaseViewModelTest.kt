package com.infinum.dbinspector.ui.databases.remove

import android.content.Context
import app.cash.turbine.test
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("RemoveDatabaseViewModel tests")
internal class RemoveDatabaseViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Context>() }
            factory { mockk<UseCases.RemoveDatabase>() }
        }
    )

    @Test
    fun `Remove database successful`() = test {
        val useCase: UseCases.RemoveDatabase = get()
        val viewModel = RemoveDatabaseViewModel(
            useCase
        )

        coEvery { useCase.invoke(any()) } returns listOf(
            mockk { every { name } returns "invoices" }
        )

        viewModel.remove(
            get(),
            mockk()
        )
        advanceUntilIdle()

        coVerify(exactly = 1) { useCase.invoke(any()) }

        val state = viewModel.stateFlow.filterNotNull().first()
        assertTrue(state is RemoveDatabaseState.Removed)
        assertTrue(state.success)

        assertNull(viewModel.errorFlow.value)
    }

    @Test
    fun `Remove database failed`() = test {
        val useCase: UseCases.RemoveDatabase = get()
        val viewModel = RemoveDatabaseViewModel(
            useCase
        )

        coEvery { useCase.invoke(any()) } returns listOf()

        viewModel.remove(
            get(),
            mockk()
        )
        advanceUntilIdle()

        coVerify(exactly = 1) { useCase.invoke(any()) }

        val state = viewModel.stateFlow.filterNotNull().first()
        assertTrue(state is RemoveDatabaseState.Removed)
        assertFalse(state.success)

        assertNull(viewModel.errorFlow.value)
    }
}
