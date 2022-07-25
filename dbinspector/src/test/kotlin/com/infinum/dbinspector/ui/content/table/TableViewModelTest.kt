package com.infinum.dbinspector.ui.content.table

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("TableViewModel tests")
internal class TableViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.domain.UseCases.OpenConnection>() }
            factory { mockk<com.infinum.dbinspector.domain.UseCases.CloseConnection>() }
            factory { mockk<com.infinum.dbinspector.domain.UseCases.GetTableInfo>() }
            factory { mockk<com.infinum.dbinspector.domain.UseCases.GetTable>() }
            factory { mockk<com.infinum.dbinspector.domain.UseCases.DropTableContent>() }
        }
    )

    @Test
    fun `Get table header`() {
        val given = "my_table"
        val expected = "PRAGMA \"table_info\"(\"$given\")"

        val viewModel = TableViewModel(
            get(),
            get(),
            get(),
            get(),
            get()
        )

        val actual = viewModel.headerStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @EnumSource(Sort::class)
    fun `Get table per sort`(sort: Sort) {
        val given = "my_table"
        val expected = "SELECT * FROM \"$given\""

        val viewModel = TableViewModel(
            get(),
            get(),
            get(),
            get(),
            get()
        )

        val actual = viewModel.schemaStatement(given, null, sort)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @Test
    fun `Drop table content`() {
        val given = "my_table"
        val expected = "DELETE FROM \"$given\""

        val viewModel = TableViewModel(
            get(),
            get(),
            get(),
            get(),
            get()
        )

        val actual = viewModel.dropStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
