package com.infinum.dbinspector.domain.connection.control.converters

import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module

@DisplayName("ConnectionConverter tests")
internal class ConnectionConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf()

    @Test
    fun `Default converts to data model with same value`() =
        launch {
            val given = mockk<ConnectionParameters> {
                every { databasePath } returns "test.db"
            }
            val expected = "test.db"

            val converter = ConnectionConverter()

            val actual = test {
                converter(given)
            }
            assertEquals(expected, actual)
        }
}
