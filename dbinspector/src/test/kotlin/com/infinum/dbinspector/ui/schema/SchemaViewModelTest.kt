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
            factory { mockk<com.infinum.dbinspector.domain.UseCases.OpenConnection>() }
            factory { mockk<com.infinum.dbinspector.domain.UseCases.CloseConnection>() }
        }
    )

    @Test
    fun `Check use cases are instantiated and constructor invoked`() {
        val openUseCase: com.infinum.dbinspector.domain.UseCases.OpenConnection = get()
        val closeUseCase: com.infinum.dbinspector.domain.UseCases.CloseConnection = get()

        val viewModel = SchemaViewModel(openUseCase, closeUseCase)

        assertNotNull(openUseCase)
        assertNotNull(closeUseCase)
        assertNotNull(viewModel)
    }
}
