package com.infinum.dbinspector.domain.connection.control.mappers

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.connection.models.DatabaseConnection
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module

@DisplayName("ConnectionMapper tests")
internal class ConnectionMapperTest : BaseTest() {

    override fun modules(): List<Module> = listOf()

    @Test
    fun `Default local values maps to default domain values`() =
        test {
            val given: SQLiteDatabase = mockk()
            val expected = DatabaseConnection(database = given)

            val mapper = ConnectionMapper()

            val actual = mapper(given)

            assertEquals(expected, actual)
        }
}
