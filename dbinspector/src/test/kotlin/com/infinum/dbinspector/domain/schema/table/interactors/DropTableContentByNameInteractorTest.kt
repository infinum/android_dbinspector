package com.infinum.dbinspector.domain.schema.table.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.domain.Interactors
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

@DisplayName("DropTableContentByNameInteractor tests")
internal class DropTableContentByNameInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Sources.Local.Schema>() }
            factory<Interactors.DropTableContentByName> { DropTableContentByNameInteractor(get()) }
        }
    )

    @Test
    fun `Invoking interactor invokes source dropTriggerByName`() {
        val interactor: Interactors.DropTableContentByName = get()
        val source: Sources.Local.Schema = get()

        coEvery { source.dropTableContentByName(any()) } returns mockk()

        launch {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.dropTableContentByName(any()) }
    }
}
