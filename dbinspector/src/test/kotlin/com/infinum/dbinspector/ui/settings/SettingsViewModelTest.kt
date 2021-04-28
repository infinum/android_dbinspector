package com.infinum.dbinspector.ui.settings

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject
import org.mockito.ArgumentMatchers.anyInt

@DisplayName("Settings ViewModel tests")
internal class SettingsViewModelTest : BaseViewModelTest() {

    override val viewModel: SettingsViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.GetSettings>() }
            single { mockk<UseCases.SaveIgnoredTableName>() }
            single { mockk<UseCases.RemoveIgnoredTableName>() }
            single { mockk<UseCases.ToggleLinesLimit>() }
            single { mockk<UseCases.SaveLinesCount>() }
            single { mockk<UseCases.SaveTruncateMode>() }
            single { mockk<UseCases.SaveBlobPreviewMode>() }
            single { SettingsViewModel(get(), get(), get(), get(), get(), get(), get()) }
        }
    )

    @Test
    fun `Get current default settings`() {
        val action: suspend (Settings) -> Unit = mockk()
        val useCase: UseCases.GetSettings = get()
        val result: Settings = mockk()
        coEvery { useCase.invoke(any()) } returns result

        viewModel.load(action)

        coVerify(exactly = 1) { action.invoke(result) }
    }

    @Test
    fun `Save ignored table name`() {
        val name = "android_metadata"
        val action: suspend (String) -> Unit = mockk()
        val useCase: UseCases.SaveIgnoredTableName = get()
        val result = "android_metadata"
        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.saveIgnoredTableName(name, action)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        coVerify(exactly = 1) { action.invoke(result) }
    }

    @Test
    fun `Remove ignored table name`() {
        val name = "android_metadata"
        val action: suspend (String) -> Unit = mockk()
        val useCase: UseCases.RemoveIgnoredTableName = get()
        val result = "android_metadata"
        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.removeIgnoredTableName(name, action)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        coVerify(exactly = 1) { action.invoke(result) }
    }

    @Test
    fun `Toggle ON lines limit`() {
        val state = true
        val useCase: UseCases.ToggleLinesLimit = get()
        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.toggleLinesLimit(state)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Toggle OFF lines limit`() {
        val state = false
        val useCase: UseCases.ToggleLinesLimit = get()
        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.toggleLinesLimit(state)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Save lines limit count`() {
        val useCase: UseCases.SaveLinesCount = get()
        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.saveLinesCount(anyInt())

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Save truncate mode`() {
        val useCase: UseCases.SaveTruncateMode = get()
        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.saveTruncateMode(TruncateMode.END)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Save blob preview type`() {
        val useCase: UseCases.SaveBlobPreviewMode = get()
        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.saveBlobPreviewType(BlobPreviewMode.UTF_8)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }
}