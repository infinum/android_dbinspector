package com.infinum.dbinspector.domain.connection.control.converters

import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.shared.BaseConverterTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

@DisplayName("Connection converter tests")
internal class ConnectionConverterTest : BaseConverterTest() {

    override val converter by inject<Converters.Connection>()

    override fun modules(): List<Module> = listOf(
        module {
            single<Converters.Connection> { ConnectionConverter() }
        }
    )

    @Test
    fun `Default converts to data model with same value`() =
        launch {
            val given = ConnectionParameters(
                databasePath = "test.db"
            )
            val expected = "test.db"
            val actual = test {
                converter(given)
            }
            assertEquals(expected, actual)
        }
}
