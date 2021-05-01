package com.infinum.dbinspector.domain.connection.control.mappers

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.connection.models.DatabaseConnection
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("ConnectionMapper tests")
internal class ConnectionMapperTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<SQLiteDatabase>() }
            factory<Mappers.Connection> { ConnectionMapper() }
        }
    )

    @Test
    fun `Default local values maps to default domain values`() =
        launch {
            val given: SQLiteDatabase = mockk()
            val expected = DatabaseConnection(database = given)

            val mapper: Mappers.Connection = get()
            val actual = test {
                mapper(given)
            }

            assertEquals(expected, actual)
        }
}
