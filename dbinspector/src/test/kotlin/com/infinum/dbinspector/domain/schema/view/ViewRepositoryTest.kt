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
            single { mockk<Interactors.GetViews>() }
            single { mockk<Interactors.GetViewByName>() }
            single { mockk<Interactors.DropViewByName>() }
            single { mockk<Control.Content>() }
            factory { ViewRepository(get(), get(), get(), get()) }
        }
    )

    @Test
    fun `Instance is not null`() {
        val repository: ViewRepository = get()

        assertNotNull(repository)
    }
}
