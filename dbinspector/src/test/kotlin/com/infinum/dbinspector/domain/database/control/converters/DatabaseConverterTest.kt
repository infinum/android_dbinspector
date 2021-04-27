package com.infinum.dbinspector.domain.database.control.converters

import android.content.Context
import android.net.Uri
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.shared.BaseConverterTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject
import org.mockito.Mockito.mock

internal class DatabaseConverterTest : BaseConverterTest() {

    override val converter by inject<Converters.Database>()

    override fun modules(): List<Module> = listOf(
        module {
            single { mock(Context::class.java) }
            single<Converters.Database> { DatabaseConverter() }
        }
    )

    @Test
    fun `Invoke is not implemented and should throw AbstractMethodError`() {
        val context: Context = get()

        val given = DatabaseParameters.Get(
            context = context,
            argument = null
        )

        assertThrows<AbstractMethodError> {
            runBlockingTest {
                converter.invoke(given)
            }
        }
    }

    @Test
    fun `Get converts to data operation with same values`() =
        launch {
            val context: Context = get()

            val given = DatabaseParameters.Get(
                context = context,
                argument = "test"
            )
            val expected = Operation(
                context = context,
                argument = "test"
            )
            val actual = test {
                converter get given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Import empty list converts to data operation with empty values`() =
        launch {
            val context: Context = get()

            val given = DatabaseParameters.Import(
                context = context,
                importUris = listOf()
            )
            val expected = Operation(
                context = context,
                importUris = listOf()
            )
            val actual = test {
                converter import given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Import converts to data operation with same values`() =
        launch {
            val context: Context = get()

            val given = DatabaseParameters.Import(
                context = context,
                importUris = listOf(
                    Uri.parse("file://test.db")
                )
            )
            val expected = Operation(
                context = context,
                importUris = listOf(
                    Uri.parse("file://test.db")
                )
            )
            val actual = test {
                converter import given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Rename converts to data operation with same values`() =
        launch {
            val context: Context = get()

            val given = DatabaseParameters.Rename(
                context = context,
                databaseDescriptor = DatabaseDescriptor(
                    exists = false,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                ),
                argument = "new_test"
            )
            val expected = Operation(
                context = context,
                databaseDescriptor = DatabaseDescriptor(
                    exists = false,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                ),
                argument = "new_test"
            )
            val actual = test {
                converter rename given
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `Command converts to data operation with same values`() =
        launch {
            val context: Context = get()

            val given = DatabaseParameters.Command(
                context = context,
                databaseDescriptor = DatabaseDescriptor(
                    exists = false,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                )
            )
            val expected = Operation(
                context = context,
                databaseDescriptor = DatabaseDescriptor(
                    exists = false,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                )
            )
            val actual = test {
                converter command given
            }
            assertEquals(expected, actual)
        }
}
