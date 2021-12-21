package com.infinum.dbinspector.ui.databases.rename

import android.content.Context
import app.cash.turbine.test
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.awaitCancellation
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("RenameDatabaseViewModel tests")
internal class RenameDatabaseViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Context>() }
            factory { mockk<UseCases.RenameDatabase>() }
        }
    )

    @Test
    fun `Rename database successful`() {
        val useCase: UseCases.RenameDatabase = get()
        val viewModel = RenameDatabaseViewModel(
            useCase
        )

        coEvery { useCase.invoke(any()) } returns listOf(
            mockk { every { name } returns "invoices" }
        )

        viewModel.rename(
            get(),
            mockk(),
            "invoices"
        )

        coVerify(exactly = 1) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                val item: RenameDatabaseState? = awaitItem()
                assertTrue(item is RenameDatabaseState.Renamed)
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
    fun `Rename database failed`() {
        val useCase: UseCases.RenameDatabase = get()
        val viewModel = RenameDatabaseViewModel(
            useCase
        )

        coEvery { useCase.invoke(any()) } returns listOf()

        viewModel.rename(
            get(),
            mockk(),
            "invoices"
        )

        coVerify(exactly = 1) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                val item: Throwable? = awaitItem()
                assertTrue(item is Throwable)
                assertNull(item.message)
                assertTrue(item.stackTrace.isNotEmpty())
            }
        }
    }
}
