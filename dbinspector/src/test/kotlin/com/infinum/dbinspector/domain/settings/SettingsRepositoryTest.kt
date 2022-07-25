package com.infinum.dbinspector.domain.settings

import com.infinum.dbinspector.data.Interactors
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
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetSettings>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.SaveLinesLimit>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.SaveLinesCount>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.SaveTruncateMode>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.SaveBlobPreviewMode>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.SaveIgnoredTableName>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.RemoveIgnoredTableName>() }
            factory { mockk<com.infinum.dbinspector.domain.Control.Settings>() }
        }
    )

    @Test
    fun `Get settings calls interactor and control once`() {
        val interactor: com.infinum.dbinspector.data.Interactors.GetSettings = get()
        val control: com.infinum.dbinspector.domain.Control.Settings = get()
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

        test {
            repository.getPage(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter get any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Save lines limit calls interactor and control converter once`() {
        val interactor: com.infinum.dbinspector.data.Interactors.SaveLinesLimit = get()
        val control: com.infinum.dbinspector.domain.Control.Settings = get()
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

        test {
            repository.saveLinesLimit(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter linesLimit any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Save lines count calls interactor and control converter once`() {
        val interactor: com.infinum.dbinspector.data.Interactors.SaveLinesCount = get()
        val control: com.infinum.dbinspector.domain.Control.Settings = get()
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

        test {
            repository.saveLinesCount(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter linesCount any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Save truncate mode calls interactor and control converter once`() {
        val interactor: com.infinum.dbinspector.data.Interactors.SaveTruncateMode = get()
        val control: com.infinum.dbinspector.domain.Control.Settings = get()
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

        test {
            repository.saveTruncateMode(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter truncateMode any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Save blob preview mode calls interactor and control converter once`() {
        val interactor: com.infinum.dbinspector.data.Interactors.SaveBlobPreviewMode = get()
        val control: com.infinum.dbinspector.domain.Control.Settings = get()
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

        test {
            repository.saveBlobPreview(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter blobPreviewMode any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Save ignored table name mode calls interactor and control converter once`() {
        val interactor: com.infinum.dbinspector.data.Interactors.SaveIgnoredTableName = get()
        val control: com.infinum.dbinspector.domain.Control.Settings = get()
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

        test {
            repository.saveIgnoredTableName(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter ignoredTableName any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Remove ignored table name mode calls interactor and control converter once`() {
        val interactor: com.infinum.dbinspector.data.Interactors.RemoveIgnoredTableName = get()
        val control: com.infinum.dbinspector.domain.Control.Settings = get()
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

        test {
            repository.removeIgnoredTableName(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter ignoredTableName any() }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }
}
