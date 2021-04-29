package com.infinum.dbinspector.ui.content.table

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class TableViewModelTest : BaseViewModelTest() {

    override val viewModel: TableViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { mockk<UseCases.GetTableInfo>() }
            single { mockk<UseCases.GetTable>() }
            single { mockk<UseCases.DropTableContent>() }
            single { TableViewModel(get(), get(), get(), get(), get()) }
        }
    )

    @Test
    fun `Get table header`() {
        val given = "my_table"
        val expected = "PRAGMA \"table_info\"(\"$given\")"
        val actual = viewModel.headerStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @EnumSource(Sort::class)
    fun `Get table per sort`(sort: Sort) {
        val given = "my_table"
        val expected = "SELECT * FROM \"$given\""
        val actual = viewModel.schemaStatement(given, null, sort)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @Test
    fun `Drop table content`() {
        val given = "my_table"
        val expected = "DELETE FROM \"$given\""
        val actual = viewModel.dropStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
