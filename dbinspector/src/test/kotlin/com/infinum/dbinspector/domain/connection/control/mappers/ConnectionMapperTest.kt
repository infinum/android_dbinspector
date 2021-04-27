package com.infinum.dbinspector.domain.connection.control.mappers

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.connection.models.DatabaseConnection
import com.infinum.dbinspector.shared.BaseMapperTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject
import org.mockito.Mockito.mock

@DisplayName("Connection mapper tests")
internal class ConnectionMapperTest : BaseMapperTest() {

    override val mapper by inject<Mappers.Connection>()

    override fun modules(): List<Module> = listOf(
        module {
            single { mock(SQLiteDatabase::class.java) }
            single<Mappers.Connection> { ConnectionMapper() }
        }
    )

    @Test
    fun `Default local values maps to default domain values`() =
        launch {
            val given: SQLiteDatabase = get()
            val expected = DatabaseConnection(database = given)
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }
}
