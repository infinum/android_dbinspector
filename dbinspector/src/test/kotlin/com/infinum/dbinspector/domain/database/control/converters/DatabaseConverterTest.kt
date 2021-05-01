package com.infinum.dbinspector.domain.database.control.converters

import android.content.Context
import android.net.Uri
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("DatabaseConverter tests")
internal class DatabaseConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Context>() }
            factory<Converters.Database> { DatabaseConverter() }
        }
    )

    @Test
    fun `Invoke is not implemented and should throw AbstractMethodError`() {
        val given = mockk<DatabaseParameters>()

        val converter: Converters.Database = get()

        assertThrows<AbstractMethodError> {
            runBlockingTest {
                converter.invoke(given)
            }
        }
    }

    @Test
    fun `Get converts to data operation with same values`() =
        launch {
            val given = DatabaseParameters.Get(
                context = get(),
                argument = "test"
            )
            val expected = Operation(
                context = get(),
                argument = "test"
            )

            val converter: Converters.Database = get()

            val actual = test {
                converter get given
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Import empty list converts to data operation with empty values`() =
        launch {
            val given = DatabaseParameters.Import(
                context = get(),
                importUris = listOf()
            )
            val expected = Operation(
                context = get(),
                importUris = listOf()
            )

            val converter: Converters.Database = get()

            val actual = test {
                converter import given
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Import converts to data operation with same values`() =
        launch {
            val given = DatabaseParameters.Import(
                context = get(),
                importUris = listOf(
                    Uri.parse("file://test.db")
                )
            )
            val expected = Operation(
                context = get(),
                importUris = listOf(
                    Uri.parse("file://test.db")
                )
            )

            val converter: Converters.Database = get()

            val actual = test {
                converter import given
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Rename converts to data operation with same values`() =
        launch {
            val given = DatabaseParameters.Rename(
                context = get(),
                databaseDescriptor = DatabaseDescriptor(
                    exists = false,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                ),
                argument = "new_test"
            )
            val expected = Operation(
                context = get(),
                databaseDescriptor = DatabaseDescriptor(
                    exists = false,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                ),
                argument = "new_test"
            )

            val converter: Converters.Database = get()

            val actual = test {
                converter rename given
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Command converts to data operation with same values`() =
        launch {
            val given = DatabaseParameters.Command(
                context = get(),
                databaseDescriptor = DatabaseDescriptor(
                    exists = false,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                )
            )
            val expected = Operation(
                context = get(),
                databaseDescriptor = DatabaseDescriptor(
                    exists = false,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                )
            )

            val converter: Converters.Database = get()

            val actual = test {
                converter command given
            }

            assertEquals(expected, actual)
        }
}
