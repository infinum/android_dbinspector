package com.infinum.dbinspector.domain.shared.converters

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters
import com.infinum.dbinspector.shared.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("TruncateModeConverter tests")
internal class TruncateModeConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Converters.TruncateMode> { TruncateModeConverter() }
        }
    )

    @Test
    fun `Truncate mode START converts to data entity with same value`() =
        launch {
            val given = SettingsParameters.Truncate(
                mode = TruncateMode.START
            )
            val expected = SettingsEntity.TruncateMode.START

            val converter: Converters.TruncateMode = get()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Truncate mode MIDDLE converts to data entity with same value`() =
        launch {
            val given = SettingsParameters.Truncate(
                mode = TruncateMode.MIDDLE
            )
            val expected = SettingsEntity.TruncateMode.MIDDLE

            val converter: Converters.TruncateMode = get()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Truncate mode END converts to data entity with same value`() =
        launch {
            val given = SettingsParameters.Truncate(
                mode = TruncateMode.END
            )
            val expected = SettingsEntity.TruncateMode.END

            val converter: Converters.TruncateMode = get()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Unknown truncate mode converts to data entity with value END`() =
        launch {
            val given = SettingsParameters.Truncate(
                mode = TruncateMode.UNKNOWN
            )
            val expected = SettingsEntity.TruncateMode.END

            val converter: Converters.TruncateMode = get()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }
}
