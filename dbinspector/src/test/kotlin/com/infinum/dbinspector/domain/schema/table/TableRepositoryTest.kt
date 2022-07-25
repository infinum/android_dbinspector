package com.infinum.dbinspector.domain.schema.table

import com.infinum.dbinspector.data.Interactors
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("TableRepository tests")
internal class TableRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetTables>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetTableByName>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.DropTableContentByName>() }
            factory { mockk<com.infinum.dbinspector.domain.Control.Content>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val repository = TableRepository(get(), get(), get(), get())

        assertNotNull(repository)
    }
}
