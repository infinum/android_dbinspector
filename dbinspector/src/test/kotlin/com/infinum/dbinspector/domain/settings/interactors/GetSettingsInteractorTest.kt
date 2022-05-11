package com.infinum.dbinspector.domain.settings.interactors

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

@DisplayName("GetSettingsInteractor tests")
internal class GetSettingsInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Sources.Local.Settings>() }
        }
    )

    @Test
    fun `Invoking interactor invokes source current`() {
        val source: Sources.Local.Settings = get()
        val interactor = GetSettingsInteractor(source)

        coEvery { source.current() } returns mockk()

        test {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.current() }
    }
}
