package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.shared.BaseTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module

@DisplayName("BlobPreviewModeMapper tests")
internal class BlobPreviewModeMapperTest : BaseTest() {

    override fun modules(): List<Module> = listOf()

    @Test
    fun `Local value PLACEHOLDER maps to same domain value`() =
        launch {
            val given = SettingsEntity.BlobPreviewMode.PLACEHOLDER
            val expected = BlobPreviewMode.PLACEHOLDER

            val mapper = BlobPreviewModeMapper()

            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Local value UTF8 maps to same domain value`() =
        launch {
            val given = SettingsEntity.BlobPreviewMode.UTF8
            val expected = BlobPreviewMode.UTF_8

            val mapper = BlobPreviewModeMapper()

            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Local value HEX maps to same domain value`() =
        launch {
            val given = SettingsEntity.BlobPreviewMode.HEX
            val expected = BlobPreviewMode.HEX

            val mapper = BlobPreviewModeMapper()

            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Local value BASE64 maps to same domain value`() =
        launch {
            val given = SettingsEntity.BlobPreviewMode.BASE64
            val expected = BlobPreviewMode.BASE_64

            val mapper = BlobPreviewModeMapper()

            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Local value UNRECOGNIZED maps to default PLACEHOLDER domain value`() =
        launch {
            val given = SettingsEntity.BlobPreviewMode.UNRECOGNIZED
            val expected = BlobPreviewMode.PLACEHOLDER

            val mapper = BlobPreviewModeMapper()

            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }
}
