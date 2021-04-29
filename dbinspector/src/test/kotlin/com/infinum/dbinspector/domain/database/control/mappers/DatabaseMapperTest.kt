package com.infinum.dbinspector.domain.database.control.mappers

import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.shared.BaseMapperTest
import java.io.File
import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

@DisplayName("Database mapper tests")
internal class DatabaseMapperTest : BaseMapperTest() {

    override val mapper by inject<Mappers.Database>()

    override fun modules(): List<Module> = listOf(
        module {
            single<Mappers.Database> { DatabaseMapper() }
        }
    )

    @Test
    fun `Database name with extension maps to domain with same values for name and extension`() =
        launch {
            val given = File("test.db")
            val expected = DatabaseDescriptor(
                exists = false,
                name = "test",
                extension = "db",
                parentPath = ""
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Database name without extension maps to domain with same values for name only`() =
        launch {
            val given = File("test")
            val expected = DatabaseDescriptor(
                exists = false,
                name = "test",
                extension = "",
                parentPath = ""
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Database without name maps to domain with no values for name`() =
        launch {
            val given = File(".db")
            val expected = DatabaseDescriptor(
                exists = false,
                name = "",
                extension = "db",
                parentPath = ""
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Database without name and extension maps to domain with no values for name and extension`() =
        launch {
            val given = File("")
            val expected = DatabaseDescriptor(
                exists = false,
                name = "",
                extension = "",
                parentPath = ""
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }
}
