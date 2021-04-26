package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.shared.BaseMapperTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class BlobPreviewModeMapperTest : BaseMapperTest() {

    override val mapper by inject<Mappers.BlobPreviewMode>()

    override fun modules(): List<Module> = listOf(
        module {
            single<Mappers.BlobPreviewMode> { BlobPreviewModeMapper() }
        }
    )

    @Test
    fun `Local value PLACEHOLDER maps to same domain value`() =
        launch {
            val given = SettingsEntity.BlobPreviewMode.PLACEHOLDER
            val expected = BlobPreviewMode.PLACEHOLDER
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
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }
}
