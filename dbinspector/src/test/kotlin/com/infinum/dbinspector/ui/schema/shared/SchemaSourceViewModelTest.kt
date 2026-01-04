package com.infinum.dbinspector.ui.schema.shared

import app.cash.turbine.test
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("SchemaSourceViewModel tests")
internal class SchemaSourceViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<UseCases.OpenConnection>() }
            factory { mockk<UseCases.CloseConnection>() }
            factory { mockk<BaseUseCase<ContentParameters, Page>>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val viewModel = object : SchemaSourceViewModel(
            get(),
            get(),
            get()
        ) {
            override fun schemaStatement(query: String?): String = ""
        }.apply {
            databasePath = "test.db"
        }

        assertNotNull(viewModel)
    }

    @Test
    fun `Schema source is not null`() {
        val given = "my_statement"

        val viewModel = object : SchemaSourceViewModel(
            get(),
            get(),
            get()
        ) {
            override fun schemaStatement(query: String?): String = ""
        }.apply {
            databasePath = "test.db"
        }

        val actual = viewModel.dataSource(viewModel.databasePath, given)

        assertNotNull(actual)
    }

    @Test
    fun `Schema query successful`() = test {
        val useCase: BaseUseCase<ContentParameters, Page> = get()
        val viewModel = object : SchemaSourceViewModel(
            get(),
            get(),
            useCase
        ) {
            override fun schemaStatement(query: String?): String = ""
        }.apply {
            databasePath = "test.db"
        }

        coEvery { useCase.invoke(any()) } returns mockk()

        viewModel.query(viewModel.databasePath, "my_statement")
        advanceUntilIdle()

        coVerify(exactly = 0) { useCase.invoke(any()) }
        
        viewModel.stateFlow.test {
            val item: SchemaState? = awaitItem()
            assertTrue(item is SchemaState.Schema)
            assertNotNull(item.schema)
            expectNoEvents()
        }
        viewModel.eventFlow.test {
            expectNoEvents()
        }
        viewModel.errorFlow.test {
            assertNull(awaitItem())
            expectNoEvents()
        }
    }
}
