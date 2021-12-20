package com.infinum.dbinspector.domain.history.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.mockito.kotlin.any

@DisplayName("ClearHistoryInteractor tests")
internal class ClearHistoryInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Sources.Local.History>() }
        }
    )

    @Test
    @Disabled("Source updateData is not invoked properly.")
    fun `Invoking interactor invokes source store`() {
        val source: Sources.Local.History = get()
        val interactor = ClearHistoryInteractor(source)

        coEvery { source.store() } returns mockk {
            coEvery { updateData { mockk() } } returns mockk()
        }

        launch {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.store() }
    }
}
