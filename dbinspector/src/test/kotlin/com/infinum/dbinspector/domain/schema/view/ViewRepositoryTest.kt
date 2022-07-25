package com.infinum.dbinspector.domain.schema.view

import com.infinum.dbinspector.data.Interactors
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("ViewRepository tests")
internal class ViewRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetViews>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetViewByName>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.DropViewByName>() }
            factory { mockk<com.infinum.dbinspector.domain.Control.Content>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val repository = ViewRepository(get(), get(), get(), get())

        assertNotNull(repository)
    }
}
