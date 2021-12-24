package com.infinum.dbinspector.domain.schema.view

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

@DisplayName("ViewRepository tests")
internal class ViewRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Interactors.GetViews>() }
            factory { mockk<Interactors.GetViewByName>() }
            factory { mockk<Interactors.DropViewByName>() }
            factory { mockk<Control.Content>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val repository = ViewRepository(get(), get(), get(), get())

        assertNotNull(repository)
    }
}
