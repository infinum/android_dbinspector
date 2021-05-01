package com.infinum.dbinspector.ui.schema.shared

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
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

@DisplayName("SchemaSourceViewModel tests")
internal class SchemaSourceViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { mockk<BaseUseCase<ContentParameters, Page>>() }
            factory<SchemaSourceViewModel> {
                object : SchemaSourceViewModel(
                    get(),
                    get(),
                    get()
                ) {
                    override fun schemaStatement(query: String?): String = ""
                }.apply {
                    databasePath = "test.db"
                }
            }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val viewModel: SchemaSourceViewModel = get()
        assertNotNull(viewModel)
    }

    @Test
    fun `Schema source is not null`() {
        val given = "my_statement"

        val viewModel: SchemaSourceViewModel = get()

        val actual = viewModel.dataSource(viewModel.databasePath, given)

        assertNotNull(actual)
    }

    @Test
    @Disabled("Not sure how to test Flow and collectLatest")
    fun `Query is invoked`() {
        val name = "my_statement"
        val action: suspend (PagingData<Cell>) -> Unit = mockk()
        val useCase: BaseUseCase<ContentParameters, Page> = get()

        val viewModel: SchemaSourceViewModel = get()
        // coEvery { action.invoke(any()) } returns mockk()
        coEvery { viewModel.pageFlow(any(), any(), action) } returns Unit
        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.query(viewModel.databasePath, name, action)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        // coVerify(exactly = 1) { action.invoke(any()) }
    }
}
