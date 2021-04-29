package com.infinum.dbinspector.ui.schema.triggers

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class TriggersViewModelTest : BaseViewModelTest() {

    override val viewModel: TriggersViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { mockk<UseCases.GetTriggers>() }
            single { TriggersViewModel(get(), get(), get()) }
        }
    )

    @Test
    fun `Select all triggers`() {
        val given: String? = null
        val expected = "SELECT name FROM \"sqlite_master\" WHERE type = 'trigger' ORDER BY name ASC"
        val actual = viewModel.schemaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @Test
    fun `Search trigger by name`() {
        val given = "my_trigger"
        val expected = "SELECT name FROM \"sqlite_master\" WHERE (type = 'trigger' AND name LIKE \"%%$given%%\") ORDER BY name ASC"
        val actual = viewModel.schemaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
