package com.infinum.dbinspector.ui.content.shared

import androidx.paging.PagingData
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.shared.BaseTest
import com.infinum.dbinspector.ui.shared.headers.Header
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("ContentViewModel tests")
internal class ContentViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single(qualifier = StringQualifier("schemaInfo")) { mockk<BaseUseCase<PragmaParameters.Pragma, Page>>() }
            single(qualifier = StringQualifier("getSchema")) { mockk<BaseUseCase<ContentParameters, Page>>() }
            single(qualifier = StringQualifier("dropSchema")) { mockk<BaseUseCase<ContentParameters, Page>>() }
            factory<ContentViewModel> {
                object : ContentViewModel(
                    get(),
                    get(),
                    get(qualifier = StringQualifier("schemaInfo")),
                    get(qualifier = StringQualifier("getSchema")),
                    get(qualifier = StringQualifier("dropSchema"))
                ) {
                    override fun headerStatement(name: String): String = ""

                    override fun schemaStatement(name: String, orderBy: String?, sort: Sort): String = ""

                    override fun dropStatement(name: String): String = ""
                }.apply {
                    databasePath = "test.db"
                }
            }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val viewModel: ContentViewModel = get()

        assertNotNull(viewModel)
    }

    @Test
    fun `Content data source is not null`() {
        val given = "my_statement"

        val viewModel: ContentViewModel = get()

        val actual = viewModel.dataSource(viewModel.databasePath, given)

        assertNotNull(actual)
    }

    @Test
    @Disabled("Lambda action is not invoked")
    fun `Content header is invoked`() {
        val name = "my_content"
        val action: suspend (List<Header>) -> Unit = mockk()
        val useCase: BaseUseCase<PragmaParameters.Pragma, Page> = get(qualifier = StringQualifier("schemaInfo"))

        // coEvery { action.invoke(any()) } returns mockk()
        coEvery { useCase.invoke(any()) } returns mockk()

        val viewModel: ContentViewModel = get()

        viewModel.header(name, action)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        // coVerify(exactly = 1) { action.invoke(any()) }
    }

    @Test
    @Disabled("Lambda action is not invoked")
    fun `Content data is invoked`() {
        val name = "my_content"
        val action: suspend (PagingData<Cell>) -> Unit = mockk()
        val useCase: BaseUseCase<ContentParameters, Page> = get(qualifier = StringQualifier("getSchema"))

        // coEvery { action.invoke(any()) } returns mockk()
        coEvery { useCase.invoke(any()) } returns mockk()

        val viewModel: ContentViewModel = get()

        viewModel.query(name, null, Sort.ASCENDING, action)

//        coVerify(exactly = 1) { useCase.invoke(any()) }
        // coVerify(exactly = 1) { action.invoke(result) }
    }

    @Test
    @Disabled("Lambda action is not invoked")
    fun `Drop content is invoked`() {
        val name = "my_content"
        val action: suspend () -> Unit = mockk()
        val useCase: BaseUseCase<ContentParameters, Page> = get(qualifier = StringQualifier("dropSchema"))

        // coEvery { action.invoke(any()) } returns mockk()
        coEvery { useCase.invoke(any()) } returns mockk()

        val viewModel: ContentViewModel = get()

        viewModel.drop(name, action)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        // coVerify(exactly = 1) { action.invoke(result) }
    }
}
