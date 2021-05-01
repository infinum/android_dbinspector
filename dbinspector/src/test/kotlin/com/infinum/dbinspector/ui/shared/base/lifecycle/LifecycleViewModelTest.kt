package com.infinum.dbinspector.ui.shared.base.lifecycle

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("LifecycleViewModel tests")
internal class LifecycleViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            factory<LifecycleViewModel> {
                object : LifecycleViewModel(get(), get()) {}.apply {
                    databasePath = "test.db"
                }
            }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val viewModel: LifecycleViewModel = get()
        assertNotNull(viewModel)
    }

    @Test
    fun `Open connection invoked`() {
        val openConnectionUseCase: UseCases.OpenConnection = get()

        coEvery { openConnectionUseCase.invoke(any()) } returns Unit

        val viewModel: LifecycleViewModel = get()
        viewModel.open()

        coVerify(exactly = 1) { openConnectionUseCase.invoke(any()) }
    }

    @Test
    fun `Close connection invoked`() {
        val closeConnectionUseCase: UseCases.CloseConnection = get()

        coEvery { closeConnectionUseCase.invoke(any()) } returns Unit

        val viewModel: LifecycleViewModel = get()
        viewModel.close()

        coVerify(exactly = 1) { closeConnectionUseCase.invoke(any()) }
    }
}
