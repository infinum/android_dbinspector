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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.Assertions.assertNotNull
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
    fun `Rename database successful`() = test {
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
        advanceUntilIdle()

        coVerify(exactly = 1) { useCase.invoke(any()) }

        val state = viewModel.stateFlow.filterNotNull().first()
        assertTrue(state is RenameDatabaseState.Renamed)
        assertTrue(state.success)

        assertNull(viewModel.errorFlow.value)
    }

    @Test
    fun `Rename database failed`() = test {
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
        advanceUntilIdle()

        coVerify(exactly = 1) { useCase.invoke(any()) }

        assertNull(viewModel.stateFlow.value)

        val error = viewModel.errorFlow.filterNotNull().first()
        assertNotNull(error.message)
        assertTrue(error.stackTrace.isNotEmpty())
    }
}
