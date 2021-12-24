package com.infinum.dbinspector.domain.schema.trigger.interactors

import com.infinum.dbinspector.data.Sources
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

@DisplayName("DropTriggerByNameInteractor tests")
internal class DropTriggerByNameInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Sources.Local.Schema>() }
        }
    )

    @Test
    fun `Invoking interactor invokes source dropTriggerByName`() {
        val source: Sources.Local.Schema = get()
        val interactor = DropTriggerByNameInteractor(source)

        coEvery { source.dropTriggerByName(any()) } returns mockk()

        launch {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.dropTriggerByName(any()) }
    }
}
