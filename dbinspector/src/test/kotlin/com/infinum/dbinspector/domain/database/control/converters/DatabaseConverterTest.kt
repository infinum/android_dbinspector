package com.infinum.dbinspector.domain.database.control.converters

import android.content.Context
import android.net.Uri
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.every
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
            factory { mockk<Context>() }
        }
    )

    @Test
    fun `Invoke is not implemented and should throw AbstractMethodError`() {
        val given = mockk<DatabaseParameters>()

        val converter = DatabaseConverter()

        assertThrows<NotImplementedError> {
            blockingTest {
                converter.invoke(given)
            }
        }
    }

    @Test
    fun `Get converts to data operation with same values`() =
        test {
            val newContext: Context = get()
            val given = mockk<DatabaseParameters.Get> {
                every { context } returns newContext
                every { argument } returns "test"
            }
            val expected = Operation(
                context = newContext,
                argument = "test"
            )

            val converter = DatabaseConverter()

            val actual = converter get given

            assertEquals(expected, actual)
        }

    @Test
    fun `Import empty list converts to data operation with empty values`() =
        test {
            val newContext: Context = get()
            val given = mockk<DatabaseParameters.Import> {
                every { context } returns newContext
                every { importUris } returns listOf()
            }
            val expected = Operation(
                context = newContext,
                importUris = listOf()
            )

            val converter = DatabaseConverter()

            val actual = converter import given

            assertEquals(expected, actual)
        }

    @Test
    fun `Import converts to data operation with same values`() =
        test {
            val newContext: Context = get()
            val given = mockk<DatabaseParameters.Import> {
                every { context } returns newContext
                every { importUris } returns listOf(
                    Uri.parse("file://test.db")
                )
            }
            val expected = Operation(
                context = newContext,
                importUris = listOf(
                    Uri.parse("file://test.db")
                )
            )

            val converter = DatabaseConverter()

            val actual = converter import given

            assertEquals(expected, actual)
        }

    @Test
    fun `Rename converts to data operation with same values`() =
        test {
            val newContext: Context = get()
            val given = mockk<DatabaseParameters.Rename> {
                every { context } returns newContext
                every { databaseDescriptor } returns DatabaseDescriptor(
                    exists = true,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                )
                every { argument } returns "new_test"
            }
            val expected = Operation(
                context = newContext,
                databaseDescriptor = DatabaseDescriptor(
                    exists = true,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                ),
                argument = "new_test"
            )

            val converter = DatabaseConverter()

            val actual = converter rename given

            assertEquals(expected, actual)
        }

    @Test
    fun `Command converts to data operation with same values`() =
        test {
            val newContext: Context = get()
            val given = mockk<DatabaseParameters.Command> {
                every { context } returns newContext
                every { databaseDescriptor } returns DatabaseDescriptor(
                    exists = true,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                )
            }
            val expected = Operation(
                context = newContext,
                databaseDescriptor = DatabaseDescriptor(
                    exists = true,
                    name = "test",
                    extension = "db",
                    parentPath = ""
                )
            )

            val converter = DatabaseConverter()

            val actual = converter command given

            assertEquals(expected, actual)
        }
}
