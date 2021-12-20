package com.infinum.dbinspector.ui.content.shared

import app.cash.turbine.test
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.schema.shared.models.exceptions.DropException
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.awaitCancellation
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import org.koin.test.get
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DisplayName("ContentViewModel tests")
internal class ContentViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<UseCases.OpenConnection>() }
            factory { mockk<UseCases.CloseConnection>() }
            factory(qualifier = StringQualifier("schemaInfo")) { mockk<BaseUseCase<PragmaParameters.Pragma, Page>>() }
            factory(qualifier = StringQualifier("getSchema")) { mockk<BaseUseCase<ContentParameters, Page>>() }
            factory(qualifier = StringQualifier("dropSchema")) { mockk<BaseUseCase<ContentParameters, Page>>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val viewModel = object : ContentViewModel(
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

        assertNotNull(viewModel)
    }

    @Test
    fun `Content data source is not null`() {
        val given = "my_statement"

        val viewModel = object : ContentViewModel(
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

        val actual = viewModel.dataSource(viewModel.databasePath, given)

        assertNotNull(actual)
    }

    @Test
    fun `Content header is invoked`() {
        val useCase: BaseUseCase<PragmaParameters.Pragma, Page> = get(qualifier = StringQualifier("schemaInfo"))
        val viewModel = object : ContentViewModel(
            get(),
            get(),
            useCase,
            get(qualifier = StringQualifier("getSchema")),
            get(qualifier = StringQualifier("dropSchema"))
        ) {
            override fun headerStatement(name: String): String = ""

            override fun schemaStatement(name: String, orderBy: String?, sort: Sort): String = ""

            override fun dropStatement(name: String): String = ""
        }.apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk {
            every { cells } returns listOf(
                mockk {
                    every { text } returns "my_column"
                }
            )
        }

        viewModel.header("my_content")

        coVerify(exactly = 1) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                val item: ContentState? = awaitItem()
                assertTrue(item is ContentState.Headers)
                assertTrue(item.headers.isNotEmpty())
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

    @Test
    fun `Content data has cells`() {
        val useCase: BaseUseCase<ContentParameters, Page> = get(qualifier = StringQualifier("getSchema"))
        val viewModel = object : ContentViewModel(
            get(),
            get(),
            get(qualifier = StringQualifier("schemaInfo")),
            useCase,
            get(qualifier = StringQualifier("dropSchema"))
        ) {
            override fun headerStatement(name: String): String = ""

            override fun schemaStatement(name: String, orderBy: String?, sort: Sort): String = ""

            override fun dropStatement(name: String): String = ""
        }.apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk {
            every { cells } returns listOf(mockk())
        }

        viewModel.query("my_content", null, Sort.ASCENDING)

        coVerify(exactly = 0) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                val item: ContentState? = awaitItem()
                assertTrue(item is ContentState.Content)
                assertNotNull(item.content)
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

    @Test
    fun `Drop content successful`() {
        val useCase: BaseUseCase<ContentParameters, Page> = get(qualifier = StringQualifier("dropSchema"))
        val viewModel = object : ContentViewModel(
            get(),
            get(),
            get(qualifier = StringQualifier("schemaInfo")),
            get(qualifier = StringQualifier("getSchema")),
            useCase
        ) {
            override fun headerStatement(name: String): String = ""

            override fun schemaStatement(name: String, orderBy: String?, sort: Sort): String = ""

            override fun dropStatement(name: String): String = ""
        }.apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk {
            every { cells } returns listOf()
        }

        viewModel.drop("my_content")

        coVerify(exactly = 1) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                val item: ContentEvent? = awaitItem()
                assertTrue(item is ContentEvent.Dropped)
                awaitCancellation()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Drop content failed`() {
        val useCase: BaseUseCase<ContentParameters, Page> = get(qualifier = StringQualifier("dropSchema"))
        val viewModel = object : ContentViewModel(
            get(),
            get(),
            get(qualifier = StringQualifier("schemaInfo")),
            get(qualifier = StringQualifier("getSchema")),
            useCase
        ) {
            override fun headerStatement(name: String): String = ""

            override fun schemaStatement(name: String, orderBy: String?, sort: Sort): String = ""

            override fun dropStatement(name: String): String = ""
        }.apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk {
            every { cells } returns listOf(mockk())
        }

        viewModel.drop("my_content")

        coVerify(exactly = 1) { useCase.invoke(any()) }
        launch {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                val item: Throwable? = awaitItem()
                assertTrue(item is DropException)
                assertNotNull(item.message)
                assertEquals("Cannot perform a drop on selected schema.", item.message)
                assertTrue(item.stackTrace.isNotEmpty())
            }
        }
    }
}
