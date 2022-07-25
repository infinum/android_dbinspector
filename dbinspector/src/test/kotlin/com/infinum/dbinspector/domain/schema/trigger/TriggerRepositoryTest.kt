package com.infinum.dbinspector.domain.schema.trigger

import com.infinum.dbinspector.data.Interactors
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
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetTriggers>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetTriggerByName>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.DropTriggerByName>() }
            factory { mockk<com.infinum.dbinspector.domain.Control.Content>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val repository = TriggerRepository(get(), get(), get(), get())

        assertNotNull(repository)
    }
}
