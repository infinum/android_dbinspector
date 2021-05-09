package com.infinum.dbinspector.domain.schema.shared

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.base.BaseInteractor
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import org.koin.test.get
import org.mockito.kotlin.any

@DisplayName("SchemaRepository tests")
internal class SchemaRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single(qualifier = StringQualifier("getPage")) { mockk<BaseInteractor<Query, QueryResult>>() }
            single(qualifier = StringQualifier("getByName")) { mockk<BaseInteractor<Query, QueryResult>>() }
            single(qualifier = StringQualifier("dropByName")) { mockk<BaseInteractor<Query, QueryResult>>() }
            single { mockk<Control.Content>() }
            factory<Repositories.Schema> {
                object : SchemaRepository(
                    get(qualifier = StringQualifier("getPage")),
                    get(qualifier = StringQualifier("getByName")),
                    get(qualifier = StringQualifier("dropByName")),
                    get()
                ) {}
            }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val repository: Repositories.Schema = get()

        assertNotNull(repository)
    }

    @Test
    fun `Get schema page calls getPage interactor and Content control`() {
        val interactor: BaseInteractor<Query, QueryResult> = get(qualifier = StringQualifier("getPage"))
        val control: Control.Content = get()
        val repository: Repositories.Schema = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter.invoke(any()) } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.getPage(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter.invoke(any()) }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Get schema by name calls getByName interactor and Content control`() {
        val interactor: BaseInteractor<Query, QueryResult> = get(qualifier = StringQualifier("getByName"))
        val control: Control.Content = get()
        val repository: Repositories.Schema = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter.invoke(any()) } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.getByName(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter.invoke(any()) }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Drop schema by name calls getByName interactor and Content control`() {
        val interactor: BaseInteractor<Query, QueryResult> = get(qualifier = StringQualifier("dropByName"))
        val control: Control.Content = get()
        val repository: Repositories.Schema = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter.invoke(any()) } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.dropByName(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter.invoke(any()) }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }
}
