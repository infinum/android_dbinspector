package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.shared.BaseTest
import com.infinum.dbinspector.ui.Presentation
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("CellMapper tests")
internal class CellMapperTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Mappers.TruncateMode> { mockk<TruncateModeMapper>() }
        }
    )

    @Test
    fun `Local Field type INTEGER value maps to domain Cell with same value`() =
        test {
            val given = Field(
                type = FieldType.INTEGER,
                text = "1",
                truncate = SettingsEntity.TruncateMode.START
            )
            val expected = Cell(
                text = "1",
                truncateMode = TruncateMode.START
            )

            val truncateMapper: Mappers.TruncateMode = get()
            val mapper = CellMapper(truncateMapper)

            coEvery { truncateMapper.invoke(any()) } returns expected.truncateMode

            val actual = mapper(given)

            coVerify(exactly = 1) { truncateMapper.invoke(any()) }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNull(actual.data)
            assertNotNull(actual.text.toIntOrNull())
        }

    @Test
    fun `Local Field type FLOAT value maps to domain Cell with same value`() =
        test {
            val given = Field(
                type = FieldType.FLOAT,
                text = "1.0"
            )
            val expected = Cell(
                text = "1.0"
            )

            val truncateMapper: Mappers.TruncateMode = get()
            val mapper = CellMapper(truncateMapper)

            coEvery { truncateMapper.invoke(any()) } returns expected.truncateMode

            val actual = mapper(given)

            coVerify(exactly = 1) { truncateMapper.invoke(any()) }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNull(actual.data)
            assertNotNull(actual.text.toFloatOrNull())
        }

    @Test
    fun `Local Field type STRING value maps to domain Cell with same value`() =
        test {
            val given = Field(
                type = FieldType.STRING,
                text = "test"
            )
            val expected = Cell(
                text = "test"
            )

            val truncateMapper: Mappers.TruncateMode = get()
            val mapper = CellMapper(truncateMapper)

            coEvery { truncateMapper.invoke(any()) } returns expected.truncateMode

            val actual = mapper(given)

            coVerify(exactly = 1) { truncateMapper.invoke(any()) }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNull(actual.data)
            assertNull(actual.text.toIntOrNull())
            assertNull(actual.text.toFloatOrNull())
        }

    @Test
    fun `Local Field type BLOB value maps to domain Cell with same value in UTF8 encoded String`() =
        test {
            val given = Field(
                type = FieldType.BLOB,
                blob = byteArrayOf(116, 101, 115, 116),
                blobPreview = SettingsEntity.BlobPreviewMode.UTF8
            )
            val expected = Cell(
                text = "test",
                data = byteArrayOf(116, 101, 115, 116)
            )

            val truncateMapper: Mappers.TruncateMode = get()
            val mapper = CellMapper(truncateMapper)

            coEvery { truncateMapper.invoke(any()) } returns expected.truncateMode

            val actual = mapper(given)

            coVerify(exactly = 1) { truncateMapper.invoke(any()) }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNotNull(actual.data)
            assertNull(actual.text.toIntOrNull())
            assertNull(actual.text.toFloatOrNull())
        }

    @Test
    fun `Local Field type BLOB value maps to domain Cell with value as PLACEHOLDER`() =
        test {
            val given = Field(
                type = FieldType.BLOB,
                blob = byteArrayOf(116, 101, 115, 116),
                blobPreview = SettingsEntity.BlobPreviewMode.PLACEHOLDER
            )
            val expected = Cell(
                text = Presentation.Constants.Settings.BLOB_DATA_PLACEHOLDER,
                data = byteArrayOf(116, 101, 115, 116)
            )

            val truncateMapper: Mappers.TruncateMode = get()
            val mapper = CellMapper(truncateMapper)

            coEvery { truncateMapper.invoke(any()) } returns expected.truncateMode

            val actual = mapper(given)

            coVerify(exactly = 1) { truncateMapper.invoke(any()) }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNotNull(actual.data)
            assertNull(actual.text.toIntOrNull())
            assertNull(actual.text.toFloatOrNull())
        }

    @Test
    fun `Local Field type BLOB value maps to domain Cell with same value in HEX`() =
        test {
            val given = Field(
                type = FieldType.BLOB,
                blob = byteArrayOf(116, 101, 115, 116),
                blobPreview = SettingsEntity.BlobPreviewMode.HEX
            )
            val expected = Cell(
                text = "74657374",
                data = byteArrayOf(116, 101, 115, 116)
            )

            val truncateMapper: Mappers.TruncateMode = get()
            val mapper = CellMapper(truncateMapper)

            coEvery { truncateMapper.invoke(any()) } returns expected.truncateMode

            val actual = mapper(given)

            coVerify(exactly = 1) { truncateMapper.invoke(any()) }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNotNull(actual.data)
        }

    @Test
    fun `Local Field type BLOB value maps to domain Cell with same value encoded with BASE64`() =
        test {
            val given = Field(
                type = FieldType.BLOB,
                blob = byteArrayOf(116, 101, 115, 116),
                blobPreview = SettingsEntity.BlobPreviewMode.BASE64
            )
            val expected = Cell(
                text = "dGVzdA==",
                data = byteArrayOf(116, 101, 115, 116)
            )

            val truncateMapper: Mappers.TruncateMode = get()
            val mapper = CellMapper(truncateMapper)

            coEvery { truncateMapper.invoke(any()) } returns expected.truncateMode

            val actual = mapper(given)

            coVerify(exactly = 1) { truncateMapper.invoke(any()) }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNotNull(actual.data)
        }

    @Test
    fun `Local Field type BLOB value maps to domain Cell with same value in default UTF8 encoded String`() =
        test {
            val given = Field(
                type = FieldType.BLOB,
                blob = byteArrayOf(116, 101, 115, 116),
                blobPreview = SettingsEntity.BlobPreviewMode.UNRECOGNIZED
            )
            val expected = Cell(
                text = "test",
                data = byteArrayOf(116, 101, 115, 116)
            )

            val truncateMapper: Mappers.TruncateMode = get()
            val mapper = CellMapper(truncateMapper)

            coEvery { truncateMapper.invoke(any()) } returns expected.truncateMode

            val actual = mapper(given)

            coVerify(exactly = 1) { truncateMapper.invoke(any()) }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNotNull(actual.data)
            assertNull(actual.text.toIntOrNull())
            assertNull(actual.text.toFloatOrNull())
        }
}
