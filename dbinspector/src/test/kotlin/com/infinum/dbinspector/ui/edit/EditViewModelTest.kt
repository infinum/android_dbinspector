package com.infinum.dbinspector.ui.edit

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.history.models.History
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.shared.BaseTest
import com.infinum.dbinspector.ui.shared.headers.Header
import com.infinum.dbinspector.ui.shared.views.editor.Keyword
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("EditViewModel tests")
internal class EditViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { mockk<UseCases.GetRawQueryHeaders>() }
            single { mockk<UseCases.GetRawQuery>() }
            single { mockk<UseCases.GetAffectedRows>() }
            single { mockk<UseCases.GetTables>() }
            single { mockk<UseCases.GetTableInfo>() }
            single { mockk<UseCases.GetHistory>() }
            single { mockk<UseCases.GetSimilarExecution>() }
            single { mockk<UseCases.SaveExecution>() }
            factory {
                EditViewModel(
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
            }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val viewModel: EditViewModel = get()

        assertNotNull(viewModel)
    }

    @Test
    fun `Content data source is not null`() {
        val given = "my_statement"

        val viewModel: EditViewModel = get()

        val actual = viewModel.dataSource(viewModel.databasePath, given)

        assertNotNull(actual)
    }

    @Test
    @Disabled("Lambda onData is not invoked")
    fun `Raw query content header is invoked`() {
        val query = "SELECT * FROM my_table"
        val onData: suspend (List<Header>) -> Unit = mockk()
        val onError: suspend (Throwable) -> Unit = mockk()
        val useCase: UseCases.GetRawQueryHeaders = get()

        val viewModel: EditViewModel = get()

        // coEvery { onData.invoke(any()) } returns mockk()
        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.header(query, onData, onError)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        // coVerify(exactly = 1) { onData.invoke(any()) }
    }

    @Test
    @Disabled("Lambda onData is not invoked")
    fun `Raw query content data is invoked`() {
        val query = "SELECT * FROM my_table"
        val onData: suspend (PagingData<Cell>) -> Unit = mockk()
        val onError: suspend (Throwable) -> Unit = mockk()
        val useCase: UseCases.GetRawQuery = get()

        val viewModel: EditViewModel = get()

        // coEvery { action.invoke(any()) } returns mockk()
        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.query(query, onData, onError)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        // coVerify(exactly = 1) { action.invoke(result) }
    }

    @Test
    @Disabled("Lambda onData is not invoked")
    fun `Raw query content data with affected rows is invoked`() {
        val query = "SELECT * FROM my_table"
        val onData: suspend (PagingData<Cell>) -> Unit = mockk()
        val onError: suspend (Throwable) -> Unit = mockk()
        val useCase: UseCases.GetAffectedRows = get()

        val viewModel: EditViewModel = get()

        // coEvery { action.invoke(any()) } returns mockk()
        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.query(query, onData, onError)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        // coVerify(exactly = 1) { action.invoke(result) }
    }

    @Test
    @Disabled("Use cases not invoked in proper order or at all after first one")
    fun `Collect database keywords like table and column names, view names and trigger names`() {
        val onData: suspend (List<Keyword>) -> Unit = mockk()
        val useCase: UseCases.GetTables = get()
        val useCaseTableInfo: UseCases.GetTableInfo = get()

        val viewModel: EditViewModel = get()

        // coEvery { action.invoke(any()) } returns mockk()
        coEvery { useCase.invoke(any()) } returns mockk()
        coEvery { useCaseTableInfo.invoke(any()) } returns mockk()

        viewModel.keywords(onData)

        coVerify(exactly = 3) { useCase.invoke(any()) }
        coVerify(exactly = 1) { useCaseTableInfo.invoke(any()) }
        // coVerify(exactly = 1) { action.invoke(result) }
    }

    @Test
    @Disabled("Lambda action is not invoked")
    fun `Get all history`() {
        val action: suspend (History) -> Unit = mockk()
        val useCase: UseCases.GetHistory = get()

        val viewModel: EditViewModel = get()

//        coEvery { action.invoke(any()) } returns Unit
        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.history(action)

        coVerify(exactly = 1) { useCase.invoke(any()) }
//        coVerify(exactly = 1) { action.invoke(any()) }
    }

    @Test
    @Disabled("Use case is not invoked")
    fun `Find similar execution`() {
        val given = "my_execution"
        val action: suspend (History) -> Unit = mockk()
        val useCase: UseCases.GetSimilarExecution = get()

        val viewModel: EditViewModel = get()

//        coEvery { action.invoke(any()) } returns Unit
        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.findSimilarExecution(testScope, given, action)

        coVerify(exactly = 1) { useCase.invoke(any()) }
//        coVerify(exactly = 1) { action.invoke(any()) }
    }

    @Test
    fun `Empty successful execution is not saved`() {
        val given = ""
        val useCase: UseCases.SaveExecution = get()

        val viewModel: EditViewModel = get()

        coEvery { useCase.invoke(any()) } returns mockk()

        launch { viewModel.saveSuccessfulExecution(given) }

        coVerify(exactly = 0) { useCase.invoke(any()) }
    }

    @Test
    fun `Successful execution is saved`() {
        val given = "SELECT * FROM my_table"
        val useCase: UseCases.SaveExecution = get()

        val viewModel: EditViewModel = get()

        coEvery { useCase.invoke(any()) } returns mockk()

        launch { viewModel.saveSuccessfulExecution(given) }

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Empty failed execution is not saved`() {
        val given = ""
        val useCase: UseCases.SaveExecution = get()

        val viewModel: EditViewModel = get()

        coEvery { useCase.invoke(any()) } returns mockk()

        launch { viewModel.saveFailedExecution(given) }

        coVerify(exactly = 0) { useCase.invoke(any()) }
    }

    @Test
    fun `Failed execution is saved`() {
        val given = "SELECT * FROM my_table"
        val useCase: UseCases.SaveExecution = get()

        val viewModel: EditViewModel = get()

        coEvery { useCase.invoke(any()) } returns mockk()

        launch { viewModel.saveFailedExecution(given) }

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }
}
