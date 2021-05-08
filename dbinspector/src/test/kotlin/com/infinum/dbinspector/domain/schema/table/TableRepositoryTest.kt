package com.infinum.dbinspector.domain.schema.table

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
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
            single { mockk<Interactors.GetTables>() }
            single { mockk<Interactors.GetTableByName>() }
            single { mockk<Interactors.DropTableContentByName>() }
            single { mockk<Control.Content>() }
            factory { TableRepository(get(), get(), get(), get()) }
        }
    )

    @Test
    fun `Instance is not null`() {
        val repository: TableRepository = get()

        assertNotNull(repository)
    }
}
