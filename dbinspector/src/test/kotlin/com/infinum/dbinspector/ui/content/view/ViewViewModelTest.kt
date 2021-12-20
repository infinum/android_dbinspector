package com.infinum.dbinspector.ui.content.view

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

@DisplayName("ViewViewModel tests")
internal class ViewViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<UseCases.OpenConnection>() }
            factory { mockk<UseCases.CloseConnection>() }
            factory { mockk<UseCases.GetTableInfo>() }
            factory { mockk<UseCases.GetView>() }
            factory { mockk<UseCases.DropView>() }
        }
    )

    @Test
    fun `Get view header statement is not blank`() {
        val given = "my_view"
        val expected = "PRAGMA \"table_info\"(\"$given\")"

        val viewModel = ViewViewModel(
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
    fun `Get view statement per sort is not blank`(sort: Sort) {
        val given = "my_view"
        val expected = "SELECT * FROM \"$given\""

        val viewModel = ViewViewModel(
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
    fun `Drop view statement is not blank`() {
        val given = "my_view"
        val expected = "DROP VIEW \"$given\""

        val viewModel = ViewViewModel(
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
