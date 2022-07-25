package com.infinum.dbinspector.ui.schema.triggers

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

@DisplayName("TriggersViewModel tests")
internal class TriggersViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.domain.UseCases.OpenConnection>() }
            factory { mockk<com.infinum.dbinspector.domain.UseCases.CloseConnection>() }
            factory { mockk<com.infinum.dbinspector.domain.UseCases.GetTriggers>() }
        }
    )

    @Test
    fun `Select all triggers`() {
        val given: String? = null
        val expected = "SELECT name FROM \"sqlite_master\" WHERE type = 'trigger' ORDER BY name ASC"

        val viewModel = TriggersViewModel(get(), get(), get())

        val actual = viewModel.schemaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @Test
    fun `Search trigger by name`() {
        val given = "my_trigger"
        val expected = "SELECT name FROM \"sqlite_master\" " +
            "WHERE (type = 'trigger' AND name LIKE \"%%$given%%\") ORDER BY name ASC"

        val viewModel = TriggersViewModel(get(), get(), get())

        val actual = viewModel.schemaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
