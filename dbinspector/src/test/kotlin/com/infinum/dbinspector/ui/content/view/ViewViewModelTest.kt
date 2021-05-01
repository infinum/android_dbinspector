package com.infinum.dbinspector.ui.content.view

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
}
