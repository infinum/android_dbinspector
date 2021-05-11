package com.infinum.dbinspector.domain.settings.interactors

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

@DisplayName("GetSettingsInteractor tests")
internal class GetSettingsInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Sources.Local.Settings>() }
            factory<Interactors.GetSettings> { GetSettingsInteractor(get()) }
        }
    )

    @Test
    fun `Invoking interactor invokes source current`() {
        val interactor: Interactors.GetSettings = get()
        val source: Sources.Local.Settings = get()

        coEvery { source.current() } returns mockk()

        launch {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.current() }
    }
}
