package com.infinum.dbinspector.ui.content.view

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.shared.BaseViewModelTest
import com.infinum.dbinspector.ui.shared.headers.Header
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject

internal class ViewViewModelTest : BaseViewModelTest() {

    override val viewModel: ViewViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { mockk<UseCases.GetTableInfo>() }
            single { mockk<UseCases.GetView>() }
            single { mockk<UseCases.DropView>() }
            single { ViewViewModel(get(), get(), get(), get(), get()) }
        }
    )

    @Test
    fun `Get view header statement is not blank`() {
        val given = "my_view"
        val expected = "PRAGMA \"table_info\"(\"$given\")"
        val actual = viewModel.headerStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @EnumSource(Sort::class)
    fun `Get view statement per sort is not blank`(sort: Sort) {
        val given = "my_view"
        val expected = "SELECT * FROM \"$given\""
        val actual = viewModel.schemaStatement(given, null, sort)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @Test
    fun `Drop view statement is not blank`() {
        val given = "my_view"
        val expected = "DROP VIEW \"$given\""
        val actual = viewModel.dropStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @Test
    fun `Content header is invoked`() {
        val name = "my_view"
        val action: suspend (List<Header>) -> Unit = mockk()
        val useCase: UseCases.GetTableInfo = get()

        val page: Page = mockk()
        val result: List<Header> = mockk()
        // coEvery { useCase.invoke(any()) } returns page

        viewModel.header(name, action)

        // coVerify(exactly = 1) { useCase.invoke(any()) }
        // coVerify(exactly = 1) { action.invoke(result) }
    }

    @Test
    fun `Get view is invoked`() {
        val name = "my_view"
        val action: suspend (PagingData<Cell>) -> Unit = mockk()
        val useCase: UseCases.GetView = get()

        val page: Page = mockk()
        val result: PagingData<Cell> = mockk()
        // coEvery { useCase.invoke(any()) } returns page

        viewModel.query(name, null, Sort.ASCENDING, action)

        // coVerify(exactly = 1) { useCase.invoke(any()) }
        // coVerify(exactly = 1) { action.invoke(result) }
    }

    @Test
    fun `Drop view is invoked`() {
        val name = "my_view"
        val action: suspend () -> Unit = mockk()
        val useCase: UseCases.DropView = get()

        val page: Page = mockk()
        // coEvery { useCase.invoke(any()) } returns page
        // every { page.cells.isEmpty() } returns true

        viewModel.drop(name, action)

        // coVerify(exactly = 1) { useCase.invoke(any()) }
        // coVerify(exactly = 1) { action.invoke() }
    }
}
