package com.infinum.dbinspector.ui.schema

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject

internal class SchemaViewModelTest : BaseViewModelTest() {

    override val viewModel: SchemaViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { SchemaViewModel(get(), get()) }
        }
    )

    @Test
    fun `Check use cases are instantiated`() {
        val openConnectionUseCase: UseCases.OpenConnection = get()
        val closeConnectionUseCase: UseCases.OpenConnection = get()

        kotlin.test.assertNotNull(openConnectionUseCase)
        kotlin.test.assertNotNull(closeConnectionUseCase)
    }
}
