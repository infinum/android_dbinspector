package com.infinum.dbinspector.ui.pragma.foreignkeys

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class ForeignKeysViewModelTest : BaseViewModelTest() {

    override val viewModel: ForeignKeysViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { mockk<UseCases.GetForeignKeys>() }
            single { ForeignKeysViewModel(get(), get(), get()) }
        }
    )

    @Test
    fun `Get foreign keys pragma`() {
        val given = "my_table"
        val expected = "PRAGMA \"foreign_key_list\"(\"$given\")"
        val actual = viewModel.pragmaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
