package com.infinum.dbinspector.domain.history.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.sources.local.proto.history.HistoryDataStore
import com.infinum.dbinspector.domain.Interactors
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

@DisplayName("GetExecutionInteractor tests")
internal class GetExecutionInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single<Sources.Local.History> { HistoryDataStore(mockk()) }
            factory<Interactors.GetExecution> { GetExecutionInteractor(get()) }
        }
    )

    @Test
    @Disabled("Source is not correct.")
    fun `Invoking interactor invokes source current`() {
        val interactor: Interactors.GetExecution = get()
        val source: Sources.Local.History = get()

        coEvery { source.current() } returns mockk()

        launch {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.current() }
    }
}
