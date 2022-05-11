package com.infinum.dbinspector.ui.settings

import app.cash.turbine.test
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.awaitCancellation
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.mockito.kotlin.any

@DisplayName("SettingsViewModel tests")
internal class SettingsViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<UseCases.GetSettings>() }
            factory { mockk<UseCases.SaveIgnoredTableName>() }
            factory { mockk<UseCases.RemoveIgnoredTableName>() }
            factory { mockk<UseCases.ToggleLinesLimit>() }
            factory { mockk<UseCases.SaveLinesCount>() }
            factory { mockk<UseCases.SaveTruncateMode>() }
            factory { mockk<UseCases.SaveBlobPreviewMode>() }
        }
    )

    @Test
    fun `Get current default settings`() {
        val useCase: UseCases.GetSettings = get()
        val viewModel = SettingsViewModel(
            useCase,
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )

        coEvery { useCase.invoke(any()) } returns mockk {
            every { linesLimitEnabled } returns false
            every { linesCount } returns 100
            every { truncateMode } returns TruncateMode.END
            every { blobPreviewMode } returns BlobPreviewMode.PLACEHOLDER
            every { ignoredTableNames } returns listOf()
        }

        viewModel.load()

        coVerify(exactly = 1) { useCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                val item: SettingsState? = awaitItem()
                assertTrue(item is SettingsState.Settings)
                assertFalse(item.settings.linesLimitEnabled)
                assertTrue(item.settings.linesCount == 100)
                assertEquals(item.settings.truncateMode, TruncateMode.END)
                assertEquals(item.settings.blobPreviewMode, BlobPreviewMode.PLACEHOLDER)
                assertTrue(item.settings.ignoredTableNames.isEmpty())
                awaitCancellation()
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Save ignored table name`() {
        val useCase: UseCases.SaveIgnoredTableName = get()
        val viewModel = SettingsViewModel(
            get(),
            useCase,
            get(),
            get(),
            get(),
            get(),
            get()
        )

        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.saveIgnoredTableName("android_metadata")

        coVerify(exactly = 1) { useCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                val item: SettingsEvent? = awaitItem()
                assertTrue(item is SettingsEvent.AddIgnoredTable)
                assertTrue(item.name == "android_metadata")
                awaitCancellation()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Remove ignored table name`() {
        val useCase: UseCases.RemoveIgnoredTableName = get()
        val viewModel = SettingsViewModel(
            get(),
            get(),
            useCase,
            get(),
            get(),
            get(),
            get()
        )

        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.removeIgnoredTableName("android_metadata")

        coVerify(exactly = 1) { useCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                val item: SettingsEvent? = awaitItem()
                assertTrue(item is SettingsEvent.RemoveIgnoredTable)
                assertTrue(item.name == "android_metadata")
                awaitCancellation()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Toggle ON lines limit`() {
        val useCase: UseCases.ToggleLinesLimit = get()
        val viewModel = SettingsViewModel(
            get(),
            get(),
            get(),
            useCase,
            get(),
            get(),
            get()
        )

        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.toggleLinesLimit(true)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                assertNull(awaitItem())
            }
        }
    }

    @Test
    fun `Toggle OFF lines limit`() {
        val useCase: UseCases.ToggleLinesLimit = get()
        val viewModel = SettingsViewModel(
            get(),
            get(),
            get(),
            useCase,
            get(),
            get(),
            get()
        )

        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.toggleLinesLimit(false)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                assertNull(awaitItem())
            }
        }
    }

    @Test
    fun `Save lines limit count`() {
        val useCase: UseCases.SaveLinesCount = get()
        val viewModel = SettingsViewModel(
            get(),
            get(),
            get(),
            get(),
            useCase,
            get(),
            get()
        )

        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.saveLinesCount(any())

        coVerify(exactly = 1) { useCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                assertNull(awaitItem())
            }
        }
    }

    @ParameterizedTest
    @EnumSource(TruncateMode::class)
    fun `Save truncate per mode`(truncateMode: TruncateMode) {
        test {
            val useCase: UseCases.SaveTruncateMode = get()
            val viewModel = SettingsViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                useCase,
                get()
            )

            coEvery { useCase.invoke(any()) } returns Unit

            viewModel.saveTruncateMode(truncateMode)

            coVerify(exactly = 1) { useCase.invoke(any()) }
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                assertNull(awaitItem())
            }
        }
    }

    @ParameterizedTest
    @EnumSource(BlobPreviewMode::class)
    fun `Save blob preview per mode`(blobPreviewMode: BlobPreviewMode) {
        val useCase: UseCases.SaveBlobPreviewMode = get()
        val viewModel = SettingsViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            useCase
        )

        coEvery { useCase.invoke(any()) } returns Unit

        viewModel.saveBlobPreviewType(blobPreviewMode)

        coVerify(exactly = 1) { useCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                assertNull(awaitItem())
            }
        }
    }
}
