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
            single { mockk<Interactors.GetTriggers>() }
            single { mockk<Interactors.GetTriggerByName>() }
            single { mockk<Interactors.DropTriggerByName>() }
            single { mockk<Control.Content>() }
            factory { TriggerRepository(get(), get(), get(), get()) }
        }
    )

    @Test
    fun `Instance is not null`() {
        val repository: TriggerRepository = get()

        assertNotNull(repository)
    }
}
