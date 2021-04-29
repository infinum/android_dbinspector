package com.infinum.dbinspector.ui.pragma

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.mockk
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject

internal class PragmaViewModelTest : BaseViewModelTest() {

    override val viewModel: PragmaViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { PragmaViewModel(get(), get()) }
        }
    )

    @Test
    fun `Check use cases are instantiated`() {
        val openConnectionUseCase: UseCases.OpenConnection = get()
        val closeConnectionUseCase: UseCases.OpenConnection = get()

        assertNotNull(openConnectionUseCase)
        assertNotNull(closeConnectionUseCase)
    }
}
