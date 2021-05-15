package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.sources.local.proto.settings.SettingsDataStore
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("SaveIgnoredTableNameInteractor tests")
internal class SaveIgnoredTableNameInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single<Sources.Local.Settings> { mockk<SettingsDataStore>() }
            factory<Interactors.SaveIgnoredTableName> { SaveIgnoredTableNameInteractor(get()) }
        }
    )

    @Test
    fun `Non empty or blank table name should save in data source`() {
        val given: SettingsTask = mockk {
            every { ignoredTableName } returns "android_metadata"
        }
        val interactor: Interactors.SaveIgnoredTableName = get()
        val source: Sources.Local.Settings = get()

        coEvery { source.store() } returns mockk {
            coEvery { updateData(any()) } returns mockk {
                coEvery { ignoredTableNamesList } returns listOf(
                    mockk {
                        every { name } returns "android_metadata"
                    }
                )
            }
        }

        launch {
            interactor.invoke(given)
        }

        coVerify(exactly = 1) { source.store() }
    }

    @Test
    fun `Empty or blank table name should not save in data source and throws exception`() {
        val given: SettingsTask = mockk {
            every { ignoredTableName } returns ""
        }
        val interactor: Interactors.SaveIgnoredTableName = get()
        val source: Sources.Local.Settings = get()

        coEvery { source.store() } returns mockk {
            coEvery { updateData(any()) } returns mockk {
                coEvery { ignoredTableNamesList } returns listOf()
            }
        }

        launch {
            assertThrows<IllegalStateException> {
                interactor.invoke(given)
            }
        }

        coVerify(exactly = 0) { source.store() }
    }
}
