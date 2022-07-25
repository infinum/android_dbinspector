package com.infinum.dbinspector.ui.pragma.shared

import app.cash.turbine.test
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertTrue
import kotlinx.coroutines.awaitCancellation
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("PragmaSourceViewModel tests")
internal class PragmaSourceViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.domain.UseCases.OpenConnection>() }
            factory { mockk<com.infinum.dbinspector.domain.UseCases.CloseConnection>() }
            factory { mockk<BaseUseCase<PragmaParameters.Pragma, Page>>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val viewModel = object : PragmaSourceViewModel(
            get(),
            get(),
            get()
        ) {
            override fun pragmaStatement(name: String): String = ""
        }.apply {
            databasePath = "test.db"
        }

        assertNotNull(viewModel)
    }

    @Test
    fun `Pragma source is not null`() {
        val given = "my_statement"

        val viewModel = object : PragmaSourceViewModel(
            get(),
            get(),
            get()
        ) {
            override fun pragmaStatement(name: String): String = ""
        }.apply {
            databasePath = "test.db"
        }

        val actual = viewModel.dataSource(viewModel.databasePath, given)

        assertNotNull(actual)
    }

    @Test
    fun `Pragma query successful`() {
        val useCase: BaseUseCase<PragmaParameters.Pragma, Page> = get()
        val viewModel = object : PragmaSourceViewModel(
            get(),
            get(),
            useCase
        ) {
            override fun pragmaStatement(name: String): String = ""
        }.apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.query("my_statement")

        coVerify(exactly = 0) { useCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                val item: PragmaState? = awaitItem()
                assertTrue(item is PragmaState.Pragma)
                assertNotNull(item.pragma)
                awaitCancellation()
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }
}
