package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.shared.BaseTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("TruncateModeMapper tests")
internal class TruncateModeMapperTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Mappers.TruncateMode> { TruncateModeMapper() }
        }
    )

    @Test
    fun `Local value START maps to same domain value`() =
        launch {
            val given = SettingsEntity.TruncateMode.START
            val expected = TruncateMode.START

            val mapper: Mappers.TruncateMode = get()

            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Local value MIDDLE maps to same domain value`() =
        launch {
            val given = SettingsEntity.TruncateMode.MIDDLE
            val expected = TruncateMode.MIDDLE

            val mapper: Mappers.TruncateMode = get()

            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Local value END maps to same domain value`() =
        launch {
            val given = SettingsEntity.TruncateMode.END
            val expected = TruncateMode.END

            val mapper: Mappers.TruncateMode = get()

            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Local value UNRECOGNIZED maps to UNKNOWN domain value`() =
        launch {
            val given = SettingsEntity.TruncateMode.UNRECOGNIZED
            val expected = TruncateMode.UNKNOWN

            val mapper: Mappers.TruncateMode = get()

            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }
}
