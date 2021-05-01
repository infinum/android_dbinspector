package com.infinum.dbinspector.ui.edit

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.shared.BaseTest
import com.infinum.dbinspector.ui.shared.headers.Header
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
}
