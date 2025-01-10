package com.infinum.dbinspector.ui.shared.base.lifecycle

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
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
            factory { mockk<UseCases.OpenConnection>() }
            factory { mockk<UseCases.CloseConnection>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        test {
            val viewModel = object : LifecycleViewModel<Any, Any>(
                get(),
                get()
            ) {}.apply {
                databasePath = "test.db"
            }

            assertNotNull(viewModel)
        }
    }

    @Test
    fun `Open connection invoked`() {
        test {
            val useCase: UseCases.OpenConnection = get()

            coEvery { useCase.invoke(any()) } returns Unit

            val viewModel = object : LifecycleViewModel<Any, Any>(
                useCase,
                get()
            ) {}.apply {
                databasePath = "test.db"
            }

            viewModel.open()
            advanceUntilIdle()

            coVerify(exactly = 1) { useCase.invoke(any()) }
        }
    }

    @Test
    fun `Close connection invoked`() {
        test {
            val useCase: UseCases.CloseConnection = get()

            coEvery { useCase.invoke(any()) } returns Unit

            val viewModel = object : LifecycleViewModel<Any, Any>(
                get(),
                useCase
            ) {}.apply {
                databasePath = "test.db"
            }

            viewModel.close()
            advanceUntilIdle()

            coVerify(exactly = 1) { useCase.invoke(any()) }
        }
    }
}
