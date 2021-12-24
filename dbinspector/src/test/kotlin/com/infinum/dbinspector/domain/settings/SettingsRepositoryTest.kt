package com.infinum.dbinspector.domain.settings

import com.infinum.dbinspector.domain.Control
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

@DisplayName("SettingsRepository tests")
internal class SettingsRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Interactors.GetSettings>() }
            factory { mockk<Interactors.SaveLinesLimit>() }
            factory { mockk<Interactors.SaveLinesCount>() }
            factory { mockk<Interactors.SaveTruncateMode>() }
            factory { mockk<Interactors.SaveBlobPreviewMode>() }
            factory { mockk<Interactors.SaveIgnoredTableName>() }
            factory { mockk<Interactors.RemoveIgnoredTableName>() }
            factory { mockk<Control.Settings>() }
        }
    )

    @Test
    fun `Get settings calls interactor and control once`() {
        val interactor: Interactors.GetSettings = get()
        val control: Control.Settings = get()
        val repository = SettingsRepository(
            interactor,
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            control
        )

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
    fun `Save lines limit calls interactor and control converter once`() {
        val interactor: Interactors.SaveLinesLimit = get()
        val control: Control.Settings = get()
        val repository = SettingsRepository(
            get(),
            interactor,
            get(),
            get(),
            get(),
            get(),
            get(),
            control
        )

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
    fun `Save lines count calls interactor and control converter once`() {
        val interactor: Interactors.SaveLinesCount = get()
        val control: Control.Settings = get()
        val repository = SettingsRepository(
            get(),
            get(),
            interactor,
            get(),
            get(),
            get(),
            get(),
            control
        )

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
    fun `Save truncate mode calls interactor and control converter once`() {
        val interactor: Interactors.SaveTruncateMode = get()
        val control: Control.Settings = get()
        val repository = SettingsRepository(
            get(),
            get(),
            get(),
            interactor,
            get(),
            get(),
            get(),
            control
        )

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
    fun `Save blob preview mode calls interactor and control converter once`() {
        val interactor: Interactors.SaveBlobPreviewMode = get()
        val control: Control.Settings = get()
        val repository = SettingsRepository(
            get(),
            get(),
            get(),
            get(),
            interactor,
            get(),
            get(),
            control
        )

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
    fun `Save ignored table name mode calls interactor and control converter once`() {
        val interactor: Interactors.SaveIgnoredTableName = get()
        val control: Control.Settings = get()
        val repository = SettingsRepository(
            get(),
            get(),
            get(),
            get(),
            get(),
            interactor,
            get(),
            control
        )

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
    fun `Remove ignored table name mode calls interactor and control converter once`() {
        val interactor: Interactors.RemoveIgnoredTableName = get()
        val control: Control.Settings = get()
        val repository = SettingsRepository(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            interactor,
            control
        )

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
