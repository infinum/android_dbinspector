package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.shared.BaseMapperTest
import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

@DisplayName("Truncate mode mapper tests")
internal class TruncateModeMapperTest : BaseMapperTest() {

    override val mapper by inject<Mappers.TruncateMode>()

    override fun modules(): List<Module> = listOf(
        module {
            single<Mappers.TruncateMode> { TruncateModeMapper() }
        }
    )

    @Test
    fun `Local value START maps to same domain value`() =
        launch {
            val given = SettingsEntity.TruncateMode.START
            val expected = TruncateMode.START
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
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }
}
