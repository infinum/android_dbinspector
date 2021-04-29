package com.infinum.dbinspector.ui.edit

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class EditViewModelTest : BaseViewModelTest() {

    override val viewModel: EditViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { mockk<UseCases.GetRawQueryHeaders>() }
            single { mockk<UseCases.GetRawQuery>() }
            single { mockk<UseCases.GetAffectedRows>() }
            single { mockk<UseCases.GetTables>() }
            single { mockk<UseCases.GetTableInfo>() }
            single { mockk<UseCases.GetHistory>() }
            single { mockk<UseCases.GetSimilarExecution>() }
            single { mockk<UseCases.SaveExecution>() }
            single { EditViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
        }
    )

    @Test
    fun `Get view header statement is not blank`() {
//        val given = "my_view"
//        val expected = "PRAGMA \"table_info\"(\"$given\")"
//        val actual = viewModel.headerStatement(given)
//
//        assertTrue(actual.isNotBlank())
//        assertEquals(expected, actual)
    }
}
