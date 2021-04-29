package com.infinum.dbinspector.ui.pragma.indexes

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class IndexViewModelTest : BaseViewModelTest() {

    override val viewModel: IndexViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { mockk<UseCases.GetIndexes>() }
            single { IndexViewModel(get(), get(), get()) }
        }
    )

    @Test
    fun `Get indexes pragma`() {
        val given = "my_table"
        val expected = "PRAGMA \"index_list\"(\"$given\")"
        val actual = viewModel.pragmaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
