package com.infinum.dbinspector.domain.shared.converters

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module

@DisplayName("TruncateModeConverter tests")
internal class TruncateModeConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf()

    @Test
    fun `Truncate mode START converts to data entity with same value`() =
        test {
            val given = mockk<SettingsParameters.Truncate> {
                every { mode } returns TruncateMode.START
            }
            val expected = SettingsEntity.TruncateMode.START

            val converter = TruncateModeConverter()

            val actual = converter(given)

            assertEquals(expected, actual)
        }

    @Test
    fun `Truncate mode MIDDLE converts to data entity with same value`() =
        test {
            val given = mockk<SettingsParameters.Truncate> {
                every { mode } returns TruncateMode.MIDDLE
            }
            val expected = SettingsEntity.TruncateMode.MIDDLE

            val converter = TruncateModeConverter()

            val actual = converter(given)

            assertEquals(expected, actual)
        }

    @Test
    fun `Truncate mode END converts to data entity with same value`() =
        test {
            val given = mockk<SettingsParameters.Truncate> {
                every { mode } returns TruncateMode.END
            }
            val expected = SettingsEntity.TruncateMode.END

            val converter = TruncateModeConverter()

            val actual = converter(given)

            assertEquals(expected, actual)
        }

    @Test
    fun `Unknown truncate mode converts to data entity with value END`() =
        test {
            val given = mockk<SettingsParameters.Truncate> {
                every { mode } returns TruncateMode.UNKNOWN
            }
            val expected = SettingsEntity.TruncateMode.END

            val converter = TruncateModeConverter()

            val actual = converter(given)

            assertEquals(expected, actual)
        }
}
