package com.infinum.dbinspector.ui.databases.remove

import android.content.Context
import app.cash.turbine.test
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.awaitCancellation
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("RemoveDatabaseViewModel tests")
internal class RemoveDatabaseViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Context>() }
            factory { mockk<UseCases.RemoveDatabase>() }
        }
    )

    @Test
    fun `Remove database successful`() {
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

        coVerify(exactly = 1) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                val item: RemoveDatabaseState? = awaitItem()
                assertTrue(item is RemoveDatabaseState.Removed)
                assertTrue(item.success)
                awaitCancellation()
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Remove database failed`() {
        val useCase: UseCases.RemoveDatabase = get()
        val viewModel = RemoveDatabaseViewModel(
            useCase
        )

        coEvery { useCase.invoke(any()) } returns listOf()

        viewModel.remove(
            get(),
            mockk()
        )

        coVerify(exactly = 1) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                val item: RemoveDatabaseState? = awaitItem()
                assertTrue(item is RemoveDatabaseState.Removed)
                assertFalse(item.success)
                awaitCancellation()
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }
}
