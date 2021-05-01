package com.infinum.dbinspector.ui.pragma.shared

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject

internal class PragmaSourceViewModelTest : BaseViewModelTest() {

    override val viewModel: PragmaSourceViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { mockk<BaseUseCase<PragmaParameters.Pragma, Page>>() }
            single<PragmaSourceViewModel> {
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
        assertNotNull(viewModel)
    }

    @Test
    fun `Pragma source is not null`() {
        val given = "my_statement"
        val actual = viewModel.dataSource(viewModel.databasePath, given)

        assertNotNull(actual)
    }

    @Test
    @Disabled("Not sure how to test Flow and collectLatest")
    fun `Query is invoked`() {
        val name = "my_statement"
        val action: suspend (PagingData<Cell>) -> Unit = mockk()
        val useCase: BaseUseCase<PragmaParameters.Pragma, Page> = get()

        // coEvery { action.invoke(any()) } returns mockk()
        coEvery { viewModel.pageFlow(any(), any(), action) } returns Unit
        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.query(name, action)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        // coVerify(exactly = 1) { action.invoke(any()) }
    }
}
