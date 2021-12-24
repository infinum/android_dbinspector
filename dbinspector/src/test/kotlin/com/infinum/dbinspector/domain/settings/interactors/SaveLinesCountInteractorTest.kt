package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.sources.local.proto.settings.SettingsDataStore
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("SaveLinesCountInteractor tests")
internal class SaveLinesCountInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Sources.Local.Settings> { mockk<SettingsDataStore>() }
        }
    )

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3, 5, 10, 20, 30, 50, 100, Int.MAX_VALUE])
    fun `Positive lines count should save in data source`(count: Int) {
        val given: SettingsTask = mockk {
            every { linesCount } returns count
        }
        val source: Sources.Local.Settings = get()
        val interactor = SaveLinesCountInteractor(source)

        coEvery { source.store() } returns mockk {
            coEvery { updateData(any()) } returns mockk {
                coEvery { linesCount } returns count
            }
        }

        launch {
            interactor.invoke(given)
        }

        coVerify(exactly = 1) { source.store() }
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, -2, -3, -5, -10, -20, -30, -50, -100, Int.MIN_VALUE])
    fun `Negative lines count should not save in data source and throw exception`(count: Int) {
        val given: SettingsTask = mockk {
            every { linesCount } returns count
        }
        val source: Sources.Local.Settings = get()
        val interactor = SaveLinesCountInteractor(source)

        coEvery { source.store() } returns mockk {
            coEvery { updateData(any()) } returns mockk {
                coEvery { linesCount } returns count
            }
        }

        launch {
            assertThrows<IllegalArgumentException> {
                interactor.invoke(given)
            }
        }

        coVerify(exactly = 0) { source.store() }
    }
}
