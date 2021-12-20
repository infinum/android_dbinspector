package com.infinum.dbinspector.domain.settings.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.data.sources.local.proto.settings.SettingsDataStore
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("SaveBlobPreviewModeInteractor tests")
internal class SaveBlobPreviewModeInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Sources.Local.Settings> { mockk<SettingsDataStore>() }
        }
    )

    @ParameterizedTest
    @EnumSource(SettingsEntity.BlobPreviewMode::class)
    fun `Blob preview mode should save in data source`(mode: SettingsEntity.BlobPreviewMode) {
        val given: SettingsTask = mockk {
            every { blobPreviewMode } returns mode
        }
        val source: Sources.Local.Settings = get()
        val interactor = SaveBlobPreviewModeInteractor(source)

        coEvery { source.store() } returns mockk {
            coEvery { updateData(any()) } returns mockk {
                coEvery { blobPreview } returns mode
            }
        }

        launch {
            interactor.invoke(given)
        }

        coVerify(exactly = 1) { source.store() }
    }
}
