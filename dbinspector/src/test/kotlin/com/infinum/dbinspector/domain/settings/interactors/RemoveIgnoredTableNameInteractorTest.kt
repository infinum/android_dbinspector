package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.sources.local.proto.settings.SettingsDataStore
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("RemoveIgnoredTableNameInteractor tests")
internal class RemoveIgnoredTableNameInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<com.infinum.dbinspector.data.Sources.Local.Settings> { mockk<SettingsDataStore>() }
        }
    )

    @Test
    fun `Non blank table name removes entity from source`() {
        val given: SettingsTask = mockk {
            every { ignoredTableName } returns "android_metadata"
        }
        val source: com.infinum.dbinspector.data.Sources.Local.Settings = get()
        val interactor =
            com.infinum.dbinspector.data.interactors.settings.RemoveIgnoredTableNameInteractor(
                source
            )

        coEvery { source.store() } returns mockk {
            every { data } returns flowOf(
                mockk {
                    every { ignoredTableNamesList } returns listOf(
                        mockk {
                            every { name } returns "android_metadata"
                        }
                    )
                }
            )
            coEvery { updateData(any()) } returns mockk {
                coEvery { ignoredTableNamesList } returns listOf()
            }
        }

        test {
            interactor.invoke(given)
        }

        coVerify(exactly = 2) { source.store() }
    }

    @Test
    fun `Empty or blank table name should not remove in data source and throws exception`() {
        val given: SettingsTask = mockk {
            every { ignoredTableName } returns ""
        }
        val source: com.infinum.dbinspector.data.Sources.Local.Settings = get()
        val interactor =
            com.infinum.dbinspector.data.interactors.settings.RemoveIgnoredTableNameInteractor(
                source
            )

        coEvery { source.store() } returns mockk {
            coEvery { updateData(any()) } returns mockk {
                coEvery { ignoredTableNamesList } returns listOf()
            }
        }

        test {
            assertThrows<IllegalStateException> {
                interactor.invoke(given)
            }
        }

        coVerify(exactly = 0) { source.store() }
    }
}
