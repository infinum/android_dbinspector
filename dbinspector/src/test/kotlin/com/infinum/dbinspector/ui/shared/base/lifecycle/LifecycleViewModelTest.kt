package com.infinum.dbinspector.ui.shared.base.lifecycle

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject

internal class LifecycleViewModelTest : BaseViewModelTest() {

    override val viewModel: LifecycleViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single<LifecycleViewModel> {
                object : LifecycleViewModel(get(), get()) {}.apply {
                    databasePath = "test.db"
                }
            }
        }
    )

    @Test
    fun `Can be instantiated`() {
        assertNotNull(viewModel)
    }

    @Test
    fun `Open connection invoked`() {
        val openConnectionUseCase: UseCases.OpenConnection = get()

        coEvery { openConnectionUseCase.invoke(any()) } returns Unit

        viewModel.open()

        coVerify(exactly = 1) { openConnectionUseCase.invoke(any()) }
    }

    @Test
    fun `Close connection invoked`() {
        val closeConnectionUseCase: UseCases.CloseConnection = get()

        coEvery { closeConnectionUseCase.invoke(any()) } returns Unit

        viewModel.close()

        coVerify(exactly = 1) { closeConnectionUseCase.invoke(any()) }
    }
}
