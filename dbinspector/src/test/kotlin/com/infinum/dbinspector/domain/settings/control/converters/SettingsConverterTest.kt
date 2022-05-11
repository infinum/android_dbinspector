package com.infinum.dbinspector.domain.settings.control.converters

import com.infinum.dbinspector.data.Data
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.converters.BlobPreviewConverter
import com.infinum.dbinspector.domain.shared.converters.TruncateModeConverter
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("SettingsConverter tests")
internal class SettingsConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Converters.TruncateMode> { mockk<TruncateModeConverter>() }
            factory<Converters.BlobPreview> { mockk<BlobPreviewConverter>() }
        }
    )

    @Test
    fun `Invoke is not implemented and should throw AbstractMethodError`() {
        val given = mockk<SettingsParameters>()

        val converter = SettingsConverter(get(), get())

        assertThrows<NotImplementedError> {
            blockingTest {
                converter.invoke(given)
            }
        }
    }

    @Test
    fun `Get converts to default data task values`() =
        test {
            val given = mockk<SettingsParameters.Get>()
            val expected = SettingsTask()

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns mockk()

            val actual = converter get given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 0) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Lines limit enabled converts to data task with value true`() =
        test {
            val given = mockk<SettingsParameters.LinesLimit> {
                every { isEnabled } returns true
            }
            val expected = SettingsTask(linesLimited = true)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns mockk()

            val actual = converter linesLimit given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 0) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Lines limit disabled converts to data task with value false`() =
        test {
            val given = mockk<SettingsParameters.LinesLimit> {
                every { isEnabled } returns false
            }
            val expected = SettingsTask(linesLimited = false)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns mockk()

            val actual = converter linesLimit given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 0) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Lines count more than zero converts to data task with same value`() =
        test {
            val given = mockk<SettingsParameters.LinesCount> {
                every { count } returns 3
            }
            val expected = SettingsTask(linesCount = 3)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns mockk()

            val actual = converter linesCount given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 0) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Lines count set to zero converts to data task with maximum value`() =
        test {
            val given = mockk<SettingsParameters.LinesCount> {
                every { count } returns 0
            }
            val expected = SettingsTask(linesCount = Data.Constants.Settings.LINES_LIMIT_MAXIMUM)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns mockk()

            val actual = converter linesCount given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 0) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Truncate mode START converts to data task with same value`() =
        test {
            val given = mockk<SettingsParameters.Truncate> {
                every { mode } returns TruncateMode.START
            }
            val expected = SettingsTask(truncateMode = SettingsEntity.TruncateMode.START)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns SettingsEntity.TruncateMode.START
            coEvery { blobPreviewConverter.invoke(any()) } returns mockk()

            val actual = converter truncateMode given

            coVerify(exactly = 1) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 0) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Truncate mode MIDDLE converts to data task with same value`() =
        test {
            val given = mockk<SettingsParameters.Truncate> {
                every { mode } returns TruncateMode.MIDDLE
            }
            val expected = SettingsTask(truncateMode = SettingsEntity.TruncateMode.MIDDLE)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns SettingsEntity.TruncateMode.MIDDLE
            coEvery { blobPreviewConverter.invoke(any()) } returns mockk()

            val actual = converter truncateMode given

            coVerify(exactly = 1) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 0) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Truncate mode END converts to data task with same value`() =
        test {
            val given = mockk<SettingsParameters.Truncate> {
                every { mode } returns TruncateMode.END
            }
            val expected = SettingsTask(truncateMode = SettingsEntity.TruncateMode.END)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns SettingsEntity.TruncateMode.END
            coEvery { blobPreviewConverter.invoke(any()) } returns mockk()

            val actual = converter truncateMode given

            coVerify(exactly = 1) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 0) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode UNSUPPORTED converts to data task with UNRECOGNIZED value`() =
        test {
            val given = mockk<SettingsParameters.BlobPreview> {
                every { mode } returns BlobPreviewMode.UNSUPPORTED
            }
            val expected = SettingsTask(blobPreviewMode = SettingsEntity.BlobPreviewMode.UNRECOGNIZED)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns SettingsEntity.BlobPreviewMode.UNRECOGNIZED

            val actual = converter blobPreviewMode given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 1) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode PLACEHOLDER converts to data task with same value`() =
        test {
            val given = mockk<SettingsParameters.BlobPreview> {
                every { mode } returns BlobPreviewMode.PLACEHOLDER
            }
            val expected = SettingsTask(blobPreviewMode = SettingsEntity.BlobPreviewMode.PLACEHOLDER)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns SettingsEntity.BlobPreviewMode.PLACEHOLDER

            val actual = converter blobPreviewMode given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 1) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode UT8 converts to data task with same value`() =
        test {
            val given = mockk<SettingsParameters.BlobPreview> {
                every { mode } returns BlobPreviewMode.UTF_8
            }
            val expected = SettingsTask(blobPreviewMode = SettingsEntity.BlobPreviewMode.UTF8)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns SettingsEntity.BlobPreviewMode.UTF8

            val actual = converter blobPreviewMode given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 1) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode HEX converts to data task with same value`() =
        test {
            val given = mockk<SettingsParameters.BlobPreview> {
                every { mode } returns BlobPreviewMode.HEX
            }
            val expected = SettingsTask(blobPreviewMode = SettingsEntity.BlobPreviewMode.HEX)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns SettingsEntity.BlobPreviewMode.HEX

            val actual = converter blobPreviewMode given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 1) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode BASE_64 converts to data task with same value`() =
        test {
            val given = mockk<SettingsParameters.BlobPreview> {
                every { mode } returns BlobPreviewMode.BASE_64
            }
            val expected = SettingsTask(blobPreviewMode = SettingsEntity.BlobPreviewMode.BASE64)

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns SettingsEntity.BlobPreviewMode.BASE64

            val actual = converter blobPreviewMode given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 1) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Empty ignored table name converts to data task with empty value`() =
        test {
            val given = mockk<SettingsParameters.IgnoredTableName> {
                every { name } returns ""
            }
            val expected = SettingsTask(ignoredTableName = "")

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns mockk()

            val actual = converter ignoredTableName given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 0) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Ignored table name converts to data task with same value`() =
        test {
            val given = mockk<SettingsParameters.IgnoredTableName> {
                every { name } returns "android_metadata"
            }
            val expected = SettingsTask(ignoredTableName = "android_metadata")

            val truncateModeConverter: Converters.TruncateMode = get()
            val blobPreviewConverter: Converters.BlobPreview = get()
            val converter = SettingsConverter(truncateModeConverter, blobPreviewConverter)

            coEvery { truncateModeConverter.invoke(any()) } returns mockk()
            coEvery { blobPreviewConverter.invoke(any()) } returns mockk()

            val actual = converter ignoredTableName given

            coVerify(exactly = 0) { truncateModeConverter.invoke(any()) }
            coVerify(exactly = 0) { blobPreviewConverter.invoke(any()) }
            assertEquals(expected, actual)
        }
}
