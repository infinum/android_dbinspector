package com.infinum.dbinspector.domain.settings.control.converters

import com.infinum.dbinspector.data.Data
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.converters.BlobPreviewConverter
import com.infinum.dbinspector.domain.shared.converters.TruncateConverter
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters
import com.infinum.dbinspector.shared.BaseConverterTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class SettingsConverterTest : BaseConverterTest() {

    override val converter by inject<Converters.Settings>()

    override fun modules(): List<Module> = listOf(
        module {
            single<Converters.Truncate> { TruncateConverter() }
            single<Converters.BlobPreview> { BlobPreviewConverter() }
            single<Converters.Settings> { SettingsConverter(get(), get()) }
        }
    )

    @Test
    fun `Get converts to default data task values`() =
        launch {
            val given = SettingsParameters.Get()
            val expected = SettingsTask()
            val actual = test {
                converter get given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Lines limit enabled converts to data task with value true`() =
        launch {
            val given = SettingsParameters.LinesLimit(isEnabled = true)
            val expected = SettingsTask(linesLimited = true)
            val actual = test {
                converter linesLimit given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Lines limit disabled converts to data task with value false`() =
        launch {
            val given = SettingsParameters.LinesLimit(isEnabled = false)
            val expected = SettingsTask(linesLimited = false)
            val actual = test {
                converter linesLimit given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Lines count more than zero converts to data task with same value`() =
        launch {
            val given = SettingsParameters.LinesCount(count = 3)
            val expected = SettingsTask(linesCount = 3)
            val actual = test {
                converter linesCount given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Lines count set to zero converts to data task with maximum value`() =
        launch {
            val given = SettingsParameters.LinesCount(count = 0)
            val expected = SettingsTask(linesCount = Data.Constants.Settings.LINES_LIMIT_MAXIMUM)
            val actual = test {
                converter linesCount given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Truncate mode START converts to data task with same value`() =
        launch {
            val given = SettingsParameters.Truncate(mode = TruncateMode.START)
            val expected = SettingsTask(truncateMode = SettingsEntity.TruncateMode.START)
            val actual = test {
                converter truncateMode given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Truncate mode MIDDLE converts to data task with same value`() =
        launch {
            val given = SettingsParameters.Truncate(mode = TruncateMode.MIDDLE)
            val expected = SettingsTask(truncateMode = SettingsEntity.TruncateMode.MIDDLE)
            val actual = test {
                converter truncateMode given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Truncate mode END converts to data task with same value`() =
        launch {
            val given = SettingsParameters.Truncate(mode = TruncateMode.END)
            val expected = SettingsTask(truncateMode = SettingsEntity.TruncateMode.END)
            val actual = test {
                converter truncateMode given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode UNSUPPORTED converts to data task with UNRECOGNIZED value`() =
        launch {
            val given = SettingsParameters.BlobPreview(mode = BlobPreviewMode.UNSUPPORTED)
            val expected = SettingsTask(blobPreviewMode = SettingsEntity.BlobPreviewMode.UNRECOGNIZED)
            val actual = test {
                converter blobPreviewMode given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode PLACEHOLDER converts to data task with same value`() =
        launch {
            val given = SettingsParameters.BlobPreview(mode = BlobPreviewMode.PLACEHOLDER)
            val expected = SettingsTask(blobPreviewMode = SettingsEntity.BlobPreviewMode.PLACEHOLDER)
            val actual = test {
                converter blobPreviewMode given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode UT8 converts to data task with same value`() =
        launch {
            val given = SettingsParameters.BlobPreview(mode = BlobPreviewMode.UTF_8)
            val expected = SettingsTask(blobPreviewMode = SettingsEntity.BlobPreviewMode.UTF8)
            val actual = test {
                converter blobPreviewMode given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode HEX converts to data task with same value`() =
        launch {
            val given = SettingsParameters.BlobPreview(mode = BlobPreviewMode.HEX)
            val expected = SettingsTask(blobPreviewMode = SettingsEntity.BlobPreviewMode.HEX)
            val actual = test {
                converter blobPreviewMode given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode BASE_64 converts to data task with same value`() =
        launch {
            val given = SettingsParameters.BlobPreview(mode = BlobPreviewMode.BASE_64)
            val expected = SettingsTask(blobPreviewMode = SettingsEntity.BlobPreviewMode.BASE64)
            val actual = test {
                converter blobPreviewMode given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Empty ignored table name converts to data task with empty value`() =
        launch {
            val given = SettingsParameters.IgnoredTableName(name = "")
            val expected = SettingsTask(ignoredTableName = "")
            val actual = test {
                converter ignoredTableName given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Ignored table name converts to data task with same value`() =
        launch {
            val given = SettingsParameters.IgnoredTableName(name = "android_metadata")
            val expected = SettingsTask(ignoredTableName = "android_metadata")
            val actual = test {
                converter ignoredTableName given
            }
            assertEquals(expected, actual)
        }
}
