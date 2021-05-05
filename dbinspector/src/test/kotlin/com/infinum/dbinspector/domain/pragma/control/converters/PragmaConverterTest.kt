package com.infinum.dbinspector.domain.pragma.control.converters

import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.converters.SortConverter
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SortParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("PragmaConverter tests")
internal class PragmaConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single<Converters.Sort> { mockk<SortConverter>() }
            factory<Converters.Pragma> { PragmaConverter(get()) }
        }
    )

    @Test
    fun `Invoke is not implemented and should throw AbstractMethodError`() {
        val given = mockk<PragmaParameters>()

        val converter: Converters.Pragma = get()

        assertThrows<NotImplementedError> {
            runBlockingTest {
                converter.invoke(given)
            }
        }
    }

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

            val converter: Converters.Pragma = get()
            val sortConverter: Converters.Sort = get()

            coEvery { sortConverter.invoke(any()) } returns mockk()
            val actual = test {
                converter version given
            }

            coVerify(exactly = 0) { sortConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Pragma converts to data query with same values`() =
        launch {
            val given = PragmaParameters.Pragma(
                databasePath = "test.db",
                statement = "PRAGMA indexes()",
                sort = SortParameters()
            )
            val expected = Query(
                databasePath = "test.db",
                statement = "PRAGMA indexes()",
                order = Order.ASCENDING
            )

            val converter: Converters.Pragma = get()
            val sortConverter: Converters.Sort = get()

            coEvery { sortConverter.invoke(any()) } returns expected.order
            val actual = test {
                converter pragma given
            }

            coVerify(exactly = 1) { sortConverter.invoke(any()) }
            assertEquals(expected, actual)
        }
}
