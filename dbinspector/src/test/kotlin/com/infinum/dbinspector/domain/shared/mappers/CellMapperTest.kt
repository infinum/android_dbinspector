package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.shared.BaseMapperTest
import com.infinum.dbinspector.ui.Presentation
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class CellMapperTest : BaseMapperTest() {

    override val mapper by inject<Mappers.Cell>()

    override fun modules(): List<Module> = listOf(
        module {
            single<Mappers.TruncateMode> { TruncateModeMapper() }
            single<Mappers.Cell> { CellMapper(get()) }
        }
    )

    @Test
    fun `Local Field type INTEGER value maps to domain Cell with same value`() =
        launch {
            val given = Field(
                type = FieldType.INTEGER,
                text = "1"
            )
            val expected = Cell(
                text = "1"
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNull(actual.data)
            assertNotNull(actual.text.toIntOrNull())
        }

    @Test
    fun `Local Field type FLOAT value maps to domain Cell with same value`() =
        launch {
            val given = Field(
                type = FieldType.FLOAT,
                text = "1.0"
            )
            val expected = Cell(
                text = "1.0"
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNull(actual.data)
            assertNotNull(actual.text.toFloatOrNull())
        }

    @Test
    fun `Local Field type STRING value maps to domain Cell with same value`() =
        launch {
            val given = Field(
                type = FieldType.STRING,
                text = "test"
            )
            val expected = Cell(
                text = "test"
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNull(actual.data)
            assertNull(actual.text.toIntOrNull())
            assertNull(actual.text.toFloatOrNull())
        }

    @Test
    fun `Local Field type BLOB value maps to domain Cell with same value in UTF8 encoded String`() =
        launch {
            val given = Field(
                type = FieldType.BLOB,
                blob = byteArrayOf(116, 101, 115, 116),
                blobPreview = SettingsEntity.BlobPreviewMode.UTF8
            )
            val expected = Cell(
                text = "test",
                data = byteArrayOf(116, 101, 115, 116)
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNotNull(actual.data)
            assertNull(actual.text.toIntOrNull())
            assertNull(actual.text.toFloatOrNull())
        }

    @Test
    fun `Local Field type BLOB value maps to domain Cell with value as PLACEHOLDER`() =
        launch {
            val given = Field(
                type = FieldType.BLOB,
                blob = byteArrayOf(116, 101, 115, 116),
                blobPreview = SettingsEntity.BlobPreviewMode.PLACEHOLDER
            )
            val expected = Cell(
                text = Presentation.Constants.Settings.BLOB_DATA_PLACEHOLDER,
                data = byteArrayOf(116, 101, 115, 116)
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNotNull(actual.data)
            assertNull(actual.text.toIntOrNull())
            assertNull(actual.text.toFloatOrNull())
        }

    @Test
    fun `Local Field type BLOB value maps to domain Cell with same value in HEX`() =
        launch {
            val given = Field(
                type = FieldType.BLOB,
                blob = byteArrayOf(116, 101, 115, 116),
                blobPreview = SettingsEntity.BlobPreviewMode.HEX
            )
            val expected = Cell(
                text = "74657374",
                data = byteArrayOf(116, 101, 115, 116)
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNotNull(actual.data)
        }

    @Test
    fun `Local Field type BLOB value maps to domain Cell with same value encoded with BASE64`() =
        launch {
            val given = Field(
                type = FieldType.BLOB,
                blob = byteArrayOf(116, 101, 115, 116),
                blobPreview = SettingsEntity.BlobPreviewMode.BASE64
            )
            val expected = Cell(
                text = "dGVzdA==",
                data = byteArrayOf(116, 101, 115, 116)
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNotNull(actual.data)
        }

    @Test
    fun `Local Field type BLOB value maps to domain Cell with same value in default UTF8 encoded String`() =
        launch {
            val given = Field(
                type = FieldType.BLOB,
                blob = byteArrayOf(116, 101, 115, 116),
                blobPreview = SettingsEntity.BlobPreviewMode.UNRECOGNIZED
            )
            val expected = Cell(
                text = "test",
                data = byteArrayOf(116, 101, 115, 116)
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
            assertNotNull(actual.text)
            assertNotNull(actual.data)
            assertNull(actual.text.toIntOrNull())
            assertNull(actual.text.toFloatOrNull())
        }
}
