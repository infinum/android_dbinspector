package com.infinum.dbinspector.ui.pragma.tableinfo

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("TableInfoViewModel tests")
internal class TableInfoViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<UseCases.OpenConnection>() }
            factory { mockk<UseCases.CloseConnection>() }
            factory { mockk<UseCases.GetTablePragma>() }
        }
    )

    @Test
    fun `Get table info pragma`() {
        val given = "my_table"
        val expected = "PRAGMA \"table_info\"(\"$given\")"

        val viewModel = TableInfoViewModel(get(), get(), get())

        val actual = viewModel.pragmaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
