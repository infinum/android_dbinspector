package com.infinum.dbinspector.ui.edit.history

import app.cash.turbine.test
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.history.models.History
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("HistoryViewModel tests")
internal class HistoryViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<UseCases.GetHistory>() }
            factory { mockk<UseCases.ClearHistory>() }
            factory { mockk<UseCases.RemoveExecution>() }
        }
    )

    @Test
    @Disabled("Unfinished coroutines during teardown.")
    fun `Load history for database path`() = test {
        val useCase: UseCases.GetHistory = get()
        val viewModel = HistoryViewModel(
            useCase,
            get(),
            get()
        )

        coEvery { useCase.invoke(any()) } returns flow {
            mockk<History> {
                every { executions } returns listOf(
                    mockk {
                        every { statement } returns "SELECT * from artists"
                        every { timestamp } returns 1639989404L
                        every { isSuccessful } returns true
                    }
                )
            }
        }

        viewModel.history("test.db")
        advanceUntilIdle()

        coVerify(exactly = 1) { useCase.invoke(any()) }

        viewModel.stateFlow.test {
            assertNull(awaitItem())
            val item: HistoryState? = awaitItem()
            assertTrue(item is HistoryState.History)
            assertNotNull(item.history)
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.eventFlow.test {
            expectNoEvents()
        }
        viewModel.errorFlow.test {
            assertNull(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Clear history for database path`() = test {
        val useCase: UseCases.ClearHistory = get()
        val viewModel = HistoryViewModel(
            get(),
            useCase,
            get()
        )

        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.clearHistory("test.db")
        advanceUntilIdle()

        coVerify(exactly = 1) { useCase.invoke(any()) }

        assertNull(viewModel.stateFlow.value)
        assertNull(viewModel.errorFlow.value)
    }

    @Test
    fun `Remove execution from history`() = test {
        val useCase: UseCases.RemoveExecution = get()
        val viewModel = HistoryViewModel(
            get(),
            get(),
            useCase
        )

        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.clearExecution(
            "test.db",
            mockk {
                every { statement } returns "SELECT * from artists"
                every { timestamp } returns 1639989404L
                every { isSuccessful } returns true
            }
        )
        advanceUntilIdle()

        coVerify(exactly = 1) { useCase.invoke(any()) }

        assertNull(viewModel.stateFlow.value)
        assertNull(viewModel.errorFlow.value)
    }
}
