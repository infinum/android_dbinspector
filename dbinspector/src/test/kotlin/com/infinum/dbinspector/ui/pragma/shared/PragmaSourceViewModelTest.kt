package com.infinum.dbinspector.ui.pragma.shared

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("PragmaSourceViewModel tests")
internal class PragmaSourceViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { mockk<BaseUseCase<PragmaParameters.Pragma, Page>>() }
            factory<PragmaSourceViewModel> {
                object : PragmaSourceViewModel(
                    get(),
                    get(),
                    get()
                ) {
                    override fun pragmaStatement(name: String): String = ""
                }.apply {
                    databasePath = "test.db"
                }
            }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val viewModel: PragmaSourceViewModel = get()

        assertNotNull(viewModel)
    }

    @Test
    fun `Pragma source is not null`() {
        val given = "my_statement"

        val viewModel: PragmaSourceViewModel = get()

        val actual = viewModel.dataSource(viewModel.databasePath, given)

        assertNotNull(actual)
    }

    @Test
    @Disabled("Not sure how to test Flow and collectLatest")
    fun `Query is invoked`() {
        val name = "my_statement"
        val useCase: BaseUseCase<PragmaParameters.Pragma, Page> = get()

        val viewModel: PragmaSourceViewModel = get()

        // coEvery { action.invoke(any()) } returns mockk()
        coEvery { viewModel.pageFlow(any(), any()) } returns mockk()
        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.query(name)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        // TODO Lambdas are replaced
        // coVerify(exactly = 1) { action.invoke(any()) }
    }
}
