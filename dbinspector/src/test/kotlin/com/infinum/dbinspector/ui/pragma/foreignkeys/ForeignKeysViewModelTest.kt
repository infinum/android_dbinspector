package com.infinum.dbinspector.ui.pragma.foreignkeys

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

@DisplayName("ForeignKeysViewModel tests")
internal class ForeignKeysViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<UseCases.OpenConnection>() }
            factory { mockk<UseCases.CloseConnection>() }
            factory { mockk<UseCases.GetForeignKeys>() }
        }
    )

    @Test
    fun `Get foreign keys pragma`() {
        val given = "my_table"
        val expected = "PRAGMA \"foreign_key_list\"(\"$given\")"

        val viewModel = ForeignKeysViewModel(get(), get(), get())

        val actual = viewModel.pragmaStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
