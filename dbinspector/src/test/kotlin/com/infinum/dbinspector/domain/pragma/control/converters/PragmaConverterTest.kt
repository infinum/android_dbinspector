package com.infinum.dbinspector.domain.pragma.control.converters

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.converters.SortConverter
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.shared.BaseConverterTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class PragmaConverterTest : BaseConverterTest() {

    override val converter by inject<Converters.Pragma>()

    override fun modules(): List<Module> = listOf(
        module {
            single<Converters.Sort> { SortConverter() }
            single<Converters.Pragma> { PragmaConverter(get()) }
        }
    )

    @Test
    fun `Version converts to data query with same values`() =
        launch {
            val given = PragmaParameters.Version(
                databasePath = "test.db",
                statement = "PRAGMA version()"
            )
            val expected = Query(
                databasePath = "test.db",
                statement = "PRAGMA version()"
            )
            val actual = test {
                converter version given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Pragma converts to data query with same values`() =
        launch {
            val given = PragmaParameters.Pragma(
                databasePath = "test.db",
                statement = "PRAGMA indexes()"
            )
            val expected = Query(
                databasePath = "test.db",
                statement = "PRAGMA indexes()"
            )
            val actual = test {
                converter pragma given
            }
            assertEquals(expected, actual)
        }
}
