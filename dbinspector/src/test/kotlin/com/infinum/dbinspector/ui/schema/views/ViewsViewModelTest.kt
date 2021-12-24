package com.infinum.dbinspector.ui.schema.views

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

@DisplayName("ViewsViewModel tests")
internal class ViewsViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<UseCases.OpenConnection>() }
            factory { mockk<UseCases.CloseConnection>() }
            factory { mockk<UseCases.GetViews>() }
        }
    )

    @Test
    fun `Select all views`() {
        val given: String? = null
        val expected = "SELECT name FROM \"sqlite_master\" WHERE type = 'view' ORDER BY name ASC"

        val viewModel = ViewsViewModel(get(), get(), get())

        val actual = viewModel.schemaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @Test
    fun `Search view by name`() {
        val given = "my_view"
        val expected = "SELECT name FROM \"sqlite_master\" " +
            "WHERE (type = 'view' AND name LIKE \"%%$given%%\") ORDER BY name ASC"

        val viewModel = ViewsViewModel(get(), get(), get())

        val actual = viewModel.schemaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
