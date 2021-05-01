package com.infinum.dbinspector.ui.schema

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("SchemaViewModel tests")
internal class SchemaViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            factory { SchemaViewModel(get(), get()) }
        }
    )

    @Test
    fun `Check use cases are instantiated and constructor invoked`() {
        val openConnectionUseCase: UseCases.OpenConnection = get()
        val closeConnectionUseCase: UseCases.CloseConnection = get()

        val viewModel: SchemaViewModel = get()

        assertNotNull(openConnectionUseCase)
        assertNotNull(closeConnectionUseCase)
        assertNotNull(viewModel)
    }
}
