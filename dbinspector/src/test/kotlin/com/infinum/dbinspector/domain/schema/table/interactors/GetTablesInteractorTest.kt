package com.infinum.dbinspector.domain.schema.table.interactors

import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.mockito.kotlin.any

@DisplayName("GetTablesInteractor tests")
internal class GetTablesInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.data.Sources.Local.Schema>() }
        }
    )

    @Test
    fun `Invoking interactor invokes source getTables`() {
        val source: com.infinum.dbinspector.data.Sources.Local.Schema = get()
        val interactor =
            com.infinum.dbinspector.data.interactors.schema.table.GetTablesInteractor(source)

        coEvery { source.getTables(any()) } returns mockk()

        test {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.getTables(any()) }
    }
}
