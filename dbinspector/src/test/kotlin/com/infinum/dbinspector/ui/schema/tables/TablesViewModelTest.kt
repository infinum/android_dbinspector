package com.infinum.dbinspector.ui.schema.tables

import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("TablesViewModel tests")
internal class TablesViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { TablesViewModel(mockk(), mockk(), mockk()) }
        }
    )

    @Test
    fun `Select all tables`() {
        val given: String? = null
        val expected = "SELECT name FROM \"sqlite_master\" WHERE type = 'table' ORDER BY name ASC"

        val viewModel: TablesViewModel = get()

        val actual = viewModel.schemaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @Test
    fun `Search table by name`() {
        val given = "my_table"
        val expected = "SELECT name FROM \"sqlite_master\" " +
            "WHERE (type = 'table' AND name LIKE \"%%$given%%\") ORDER BY name ASC"

        val viewModel: TablesViewModel = get()

        val actual = viewModel.schemaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
