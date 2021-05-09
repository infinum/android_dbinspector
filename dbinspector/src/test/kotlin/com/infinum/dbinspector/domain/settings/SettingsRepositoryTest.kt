package com.infinum.dbinspector.domain.settings

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
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

@DisplayName("SettingsRepository tests")
internal class SettingsRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Interactors.GetSettings>() }
            single { mockk<Interactors.SaveLinesLimit>() }
            single { mockk<Interactors.SaveLinesCount>() }
            single { mockk<Interactors.SaveTruncateMode>() }
            single { mockk<Interactors.SaveBlobPreviewMode>() }
            single { mockk<Interactors.SaveIgnoredTableName>() }
            single { mockk<Interactors.RemoveIgnoredTableName>() }
            single { mockk<Control.Settings>() }
            factory<Repositories.Settings> {
                SettingsRepository(get(), get(), get(), get(), get(), get(), get(), get())
            }
        }
    )

    @Test
    fun `Get settings calls GetSettings interactor and Settings control`() {
        val interactor: Interactors.GetSettings = get()
        val control: Control.Settings = get()
        val repository: Repositories.Settings = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter get any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.getPage(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter get any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Save lines limit calls SaveLinesLimit interactor and Settings control converter`() {
        val interactor: Interactors.SaveLinesLimit = get()
        val control: Control.Settings = get()
        val repository: Repositories.Settings = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter linesLimit any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.saveLinesLimit(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter linesLimit any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Save lines count calls SaveLinesCount interactor and Settings control converter`() {
        val interactor: Interactors.SaveLinesCount = get()
        val control: Control.Settings = get()
        val repository: Repositories.Settings = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter linesCount any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.saveLinesCount(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter linesCount any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Save truncate mode calls SaveTruncateMode interactor and Settings control converter`() {
        val interactor: Interactors.SaveTruncateMode = get()
        val control: Control.Settings = get()
        val repository: Repositories.Settings = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter truncateMode any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.saveTruncateMode(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter truncateMode any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Save blob preview mode calls SaveBlobPreviewMode interactor and Settings control converter`() {
        val interactor: Interactors.SaveBlobPreviewMode = get()
        val control: Control.Settings = get()
        val repository: Repositories.Settings = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter blobPreviewMode any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.saveBlobPreview(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter blobPreviewMode any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Save ignored table name mode calls SaveIgnoredTableName interactor and Settings control converter`() {
        val interactor: Interactors.SaveIgnoredTableName = get()
        val control: Control.Settings = get()
        val repository: Repositories.Settings = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter ignoredTableName any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.saveIgnoredTableName(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter ignoredTableName any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Remove ignored table name mode calls RemoveIgnoredTableName interactor and Settings control converter`() {
        val interactor: Interactors.RemoveIgnoredTableName = get()
        val control: Control.Settings = get()
        val repository: Repositories.Settings = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter ignoredTableName any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.removeIgnoredTableName(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter ignoredTableName any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }
}
