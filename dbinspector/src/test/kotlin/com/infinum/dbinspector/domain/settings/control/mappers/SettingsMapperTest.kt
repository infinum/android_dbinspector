package com.infinum.dbinspector.domain.settings.control.mappers

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.domain.shared.mappers.BlobPreviewModeMapper
import com.infinum.dbinspector.domain.shared.mappers.TruncateModeMapper
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("SettingsMapper tests")
internal class SettingsMapperTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single<Mappers.BlobPreviewMode> { mockk<BlobPreviewModeMapper>() }
            single<Mappers.TruncateMode> { mockk<TruncateModeMapper>() }
            factory<Mappers.Settings> { SettingsMapper(get(), get()) }
        }
    )

    @Test
    fun `Default local values maps to default domain values`() =
        launch {
            val given = SettingsEntity.getDefaultInstance()
            val expected = Settings()

            val mapper: Mappers.Settings = get()
            val trucateModeMapper: Mappers.TruncateMode = get()
            val blobPreviewModeMapper: Mappers.BlobPreviewMode = get()

            coEvery { trucateModeMapper.invoke(any()) } returns expected.truncateMode
            coEvery { blobPreviewModeMapper.invoke(any()) } returns expected.blobPreviewMode
            val actual = test {
                mapper(given)
            }

            coVerify(exactly = 1) { trucateModeMapper.invoke(any()) }
            coVerify(exactly = 1) { blobPreviewModeMapper.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Local values with lines count limit more than zero maps to domain values with same lines count limit`() =
        launch {
            val given = SettingsEntity.newBuilder()
                .setLinesCount(3)
                .build()
            val expected = Settings(linesCount = 3)

            val mapper: Mappers.Settings = get()
            val trucateModeMapper: Mappers.TruncateMode = get()
            val blobPreviewModeMapper: Mappers.BlobPreviewMode = get()

            coEvery { trucateModeMapper.invoke(any()) } returns expected.truncateMode
            coEvery { blobPreviewModeMapper.invoke(any()) } returns expected.blobPreviewMode
            val actual = test {
                mapper(given)
            }

            coVerify(exactly = 1) { trucateModeMapper.invoke(any()) }
            coVerify(exactly = 1) { blobPreviewModeMapper.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Local values with lines count limit zero maps to domain values with maximum lines count limit`() =
        launch {
            val given = SettingsEntity.newBuilder()
                .setLinesCount(0)
                .build()
            val expected = Settings(linesCount = Domain.Constants.Settings.LINES_LIMIT_MAXIMUM)

            val mapper: Mappers.Settings = get()
            val trucateModeMapper: Mappers.TruncateMode = get()
            val blobPreviewModeMapper: Mappers.BlobPreviewMode = get()

            coEvery { trucateModeMapper.invoke(any()) } returns expected.truncateMode
            coEvery { blobPreviewModeMapper.invoke(any()) } returns expected.blobPreviewMode
            val actual = test {
                mapper(given)
            }

            coVerify(exactly = 1) { trucateModeMapper.invoke(any()) }
            coVerify(exactly = 1) { blobPreviewModeMapper.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Local values with ignored names maps to domain values with list of same names`() =
        launch {
            val given = SettingsEntity.newBuilder()
                .addIgnoredTableNames(
                    SettingsEntity.IgnoredTableName.newBuilder()
                        .setName("android_metadata")
                        .build()
                ).build()
            val expected = Settings(ignoredTableNames = listOf("android_metadata"))

            val mapper: Mappers.Settings = get()
            val trucateModeMapper: Mappers.TruncateMode = get()
            val blobPreviewModeMapper: Mappers.BlobPreviewMode = get()

            coEvery { trucateModeMapper.invoke(any()) } returns expected.truncateMode
            coEvery { blobPreviewModeMapper.invoke(any()) } returns expected.blobPreviewMode
            val actual = test {
                mapper(given)
            }

            coVerify(exactly = 1) { trucateModeMapper.invoke(any()) }
            coVerify(exactly = 1) { blobPreviewModeMapper.invoke(any()) }
            assertEquals(expected, actual)
            assertFalse(expected.ignoredTableNames.isEmpty())
            assertTrue(expected.ignoredTableNames.size == 1)
        }
}
