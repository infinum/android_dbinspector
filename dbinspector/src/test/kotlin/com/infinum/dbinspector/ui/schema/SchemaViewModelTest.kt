package com.infinum.dbinspector.ui.schema

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseViewModelTest
import com.infinum.dbinspector.ui.shared.base.BaseViewModel
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

internal class SchemaViewModelTest : BaseViewModelTest() {

    override val viewModel: BaseViewModel
        get() = throw NotImplementedError()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { SchemaViewModel(get(), get()) }
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
