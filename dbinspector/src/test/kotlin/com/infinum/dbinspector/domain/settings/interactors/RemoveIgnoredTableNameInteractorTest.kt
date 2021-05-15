package com.infinum.dbinspector.domain.settings.interactors

import androidx.datastore.core.DataStore
import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.data.sources.local.proto.settings.SettingsDataStore
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.firstOrNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("RemoveIgnoredTableNameInteractor tests")
internal class RemoveIgnoredTableNameInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<DataStore<SettingsEntity>>() }
            single<Sources.Local.Settings> { SettingsDataStore(get()) }
            factory<Interactors.RemoveIgnoredTableName> { RemoveIgnoredTableNameInteractor(get()) }
        }
    )

    @Test
    @Disabled("Something is wrong with mockking DataStore")
    fun `Non blank table name removes entity from source`() {
        val given: SettingsTask = mockk {
            every { ignoredTableName } returns "android_metadata"
        }
        val interactor: Interactors.RemoveIgnoredTableName = get()
//        val source: Sources.Local.Settings = get()
        val dataStore: DataStore<SettingsEntity> = get()

        coEvery { dataStore.data.firstOrNull() } returns mockk()
//        coEvery { source.store() } returns mockk()

        launch {
            interactor.invoke(given)
        }

        coVerify(exactly = 1) { dataStore.data.firstOrNull() }
    }
}
