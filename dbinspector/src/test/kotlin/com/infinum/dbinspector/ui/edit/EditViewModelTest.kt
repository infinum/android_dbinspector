package com.infinum.dbinspector.ui.edit

import app.cash.turbine.test
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseTest
import com.infinum.dbinspector.ui.content.shared.ContentState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.awaitCancellation
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DisplayName("EditViewModel tests")
internal class EditViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<UseCases.OpenConnection>() }
            factory { mockk<UseCases.CloseConnection>() }
            factory { mockk<UseCases.GetRawQueryHeaders>() }
            factory { mockk<UseCases.GetRawQuery>() }
            factory { mockk<UseCases.GetAffectedRows>() }
            factory { mockk<UseCases.GetTables>() }
            factory { mockk<UseCases.GetTableInfo>() }
            factory { mockk<UseCases.GetHistory>() }
            factory { mockk<UseCases.GetSimilarExecution>() }
            factory { mockk<UseCases.SaveExecution>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val viewModel = EditViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        ).apply {
            databasePath = "test.db"
        }

        assertNotNull(viewModel)
    }

    @Test
    fun `Content data source is not null`() {
        val given = "my_statement"

        val viewModel = EditViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        ).apply {
            databasePath = "test.db"
        }

        val actual = viewModel.dataSource(viewModel.databasePath, given)

        assertNotNull(actual)
    }

    @Test
    fun `Raw query content header is invoked`() {
        val useCase: UseCases.GetRawQueryHeaders = get()
        val viewModel = EditViewModel(
            get(),
            get(),
            useCase,
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        ).apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk {
            every { cells } returns listOf(
                mockk {
                    every { text } returns "my_column"
                }
            )
        }

        viewModel.header("SELECT * FROM my_table")

        coVerify(exactly = 1) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                val item: EditState? = awaitItem()
                assertTrue(item is EditState.Headers)
                assertTrue(item.headers.isNotEmpty())
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
    fun `Raw query content data has cells`() {
        val useCase: UseCases.GetRawQuery = get()
        val viewModel = EditViewModel(
            get(),
            get(),
            get(),
            useCase,
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        ).apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.query("SELECT * FROM my_table")

        coVerify(exactly = 0) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                val item: EditState? = awaitItem()
                assertTrue(item is EditState.Content)
                assertNotNull(item.content)
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
    fun `Raw query content data with affected rows successful`() {
        val useCase: UseCases.GetAffectedRows = get()
        val viewModel = EditViewModel(
            get(),
            get(),
            get(),
            get(),
            useCase,
            get(),
            get(),
            get(),
            get(),
            get()
        ).apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.query("SELECT * FROM my_table")

        coVerify(exactly = 0) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                val item: EditState? = awaitItem()
                assertTrue(item is EditState.Content)
                assertNotNull(item.content)
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
    fun `Collect database keywords like table and column names, view names and trigger names`() {
        val getTablesUseCase: UseCases.GetTables = get()
        val getTableInfoUseCase: UseCases.GetTableInfo = get()
        val viewModel = EditViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            getTablesUseCase,
            getTableInfoUseCase,
            get(),
            get(),
            get()
        ).apply {
            databasePath = "test.db"
        }

        coEvery { getTablesUseCase.invoke(any()) } returns mockk {
            every { cells } returns listOf(
                mockk {
                    every { text } returns "my_cell"
                }
            )
        }
        coEvery { getTableInfoUseCase.invoke(any()) } returns mockk {
            every { cells } returns listOf(
                mockk {
                    every { text } returns "my_column"
                }
            )
        }

        viewModel.keywords()

//        coVerify(exactly = 3) { getTablesUseCase.invoke(any()) }
//        coVerify(exactly = 1) { getTableInfoUseCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                val item: EditEvent? = awaitItem()
                assertTrue(item is EditEvent.Keywords)
                assertNotNull(item.keywords)
                awaitCancellation()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    @Disabled("Unfinished coroutines during teardown.")
    fun `Get all history`() {
        val useCase: UseCases.GetHistory = get()
        val viewModel = EditViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            useCase,
            get(),
            get()
        ).apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk()

        launch {
            viewModel.history()
        }

        coVerify(exactly = 1) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                val item: EditEvent? = awaitItem()
                assertTrue(item is EditEvent.History)
                assertNotNull(item.history)
                awaitCancellation()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    @Disabled("How to test with delay")
    fun `Find similar execution`() {
        val useCase: UseCases.GetSimilarExecution = get()
        val viewModel = EditViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            useCase,
            get()
        ).apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.findSimilarExecution(testScope, "my_execution")

        coVerify(exactly = 1) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                val item: EditEvent? = awaitItem()
                assertTrue(item is EditEvent.SimilarExecution)
                assertNotNull(item.history)
                awaitCancellation()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Empty successful execution is not saved`() {
        val useCase: UseCases.SaveExecution = get()
        val viewModel = EditViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            useCase
        ).apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.saveSuccessfulExecution("")

        coVerify(exactly = 0) { useCase.invoke(any()) }
    }

    @Test
    fun `Successful execution is saved`() {
        val useCase: UseCases.SaveExecution = get()
        val viewModel = EditViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            useCase
        ).apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.saveSuccessfulExecution("SELECT * FROM my_table")

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Empty failed execution is not saved`() {
        val useCase: UseCases.SaveExecution = get()
        val viewModel = EditViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            useCase
        ).apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.saveFailedExecution("")

        coVerify(exactly = 0) { useCase.invoke(any()) }
    }

    @Test
    fun `Failed execution is saved`() {
        val useCase: UseCases.SaveExecution = get()
        val viewModel = EditViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            useCase
        ).apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.saveFailedExecution( "SELECT * FROM my_table")

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }
}
