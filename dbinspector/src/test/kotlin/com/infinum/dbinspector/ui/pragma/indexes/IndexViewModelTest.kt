package com.infinum.dbinspector.ui.pragma.indexes

import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("IndexViewModel tests")
internal class IndexViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { IndexViewModel(mockk(), mockk(), mockk()) }
        }
    )

    @Test
    fun `Get indexes pragma`() {
        val given = "my_table"
        val expected = "PRAGMA \"index_list\"(\"$given\")"

        val viewModel: IndexViewModel = get()

        val actual = viewModel.pragmaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
