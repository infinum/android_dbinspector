package com.infinum.dbinspector.domain.shared.converters

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters
import com.infinum.dbinspector.shared.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("BlobPreviewConverter tests")
internal class BlobPreviewConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Converters.BlobPreview> { BlobPreviewConverter() }
        }
    )

    @Test
    fun `Blob preview mode UNSUPPORTED converts to data entity with same value`() =
        launch {
            val given = SettingsParameters.BlobPreview(
                mode = BlobPreviewMode.UNSUPPORTED
            )
            val expected = SettingsEntity.BlobPreviewMode.UNRECOGNIZED

            val converter: Converters.BlobPreview = get()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode PLACEHOLDER converts to data entity with same value`() =
        launch {
            val given = SettingsParameters.BlobPreview(
                mode = BlobPreviewMode.PLACEHOLDER
            )
            val expected = SettingsEntity.BlobPreviewMode.PLACEHOLDER

            val converter: Converters.BlobPreview = get()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode UTF_8 converts to data entity with same value`() =
        launch {
            val given = SettingsParameters.BlobPreview(
                mode = BlobPreviewMode.UTF_8
            )
            val expected = SettingsEntity.BlobPreviewMode.UTF8

            val converter: Converters.BlobPreview = get()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode HEX converts to data entity with same value`() =
        launch {
            val given = SettingsParameters.BlobPreview(
                mode = BlobPreviewMode.HEX
            )
            val expected = SettingsEntity.BlobPreviewMode.HEX

            val converter: Converters.BlobPreview = get()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Blob preview mode BASE_64 converts to data entity with same value`() =
        launch {
            val given = SettingsParameters.BlobPreview(
                mode = BlobPreviewMode.BASE_64
            )
            val expected = SettingsEntity.BlobPreviewMode.BASE64

            val converter: Converters.BlobPreview = get()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }
}
