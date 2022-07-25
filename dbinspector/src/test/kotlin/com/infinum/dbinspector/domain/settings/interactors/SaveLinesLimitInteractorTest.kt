package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.sources.local.proto.settings.SettingsDataStore
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("SaveLinesLimitInteractor tests")
internal class SaveLinesLimitInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<com.infinum.dbinspector.data.Sources.Local.Settings> { mockk<SettingsDataStore>() }
        }
    )

    @Test
    fun `Enable lines limit should save true in data source`() {
        val given: SettingsTask = mockk {
            every { linesLimited } returns true
        }
        val source: com.infinum.dbinspector.data.Sources.Local.Settings = get()
        val interactor =
            com.infinum.dbinspector.data.interactors.settings.SaveLinesLimitInteractor(source)

        coEvery { source.store() } returns mockk {
            coEvery { updateData(any()) } returns mockk {
                coEvery { linesLimit } returns true
            }
        }

        test {
            interactor.invoke(given)
        }

        coVerify(exactly = 1) { source.store() }
    }

    @Test
    fun `Disable lines limit should save false in data source`() {
        val given: SettingsTask = mockk {
            every { linesLimited } returns false
        }
        val source: com.infinum.dbinspector.data.Sources.Local.Settings = get()
        val interactor =
            com.infinum.dbinspector.data.interactors.settings.SaveLinesLimitInteractor(source)

        coEvery { source.store() } returns mockk {
            coEvery { updateData(any()) } returns mockk {
                coEvery { linesLimit } returns false
            }
        }

        test {
            interactor.invoke(given)
        }

        coVerify(exactly = 1) { source.store() }
    }
}
