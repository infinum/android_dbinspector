package com.infinum.dbinspector.domain.schema.trigger

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

@DisplayName("TriggerRepository tests")
internal class TriggerRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Interactors.GetTriggers>() }
            factory { mockk<Interactors.GetTriggerByName>() }
            factory { mockk<Interactors.DropTriggerByName>() }
            factory { mockk<Control.Content>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val repository = TriggerRepository(get(), get(), get(), get())

        assertNotNull(repository)
    }
}
