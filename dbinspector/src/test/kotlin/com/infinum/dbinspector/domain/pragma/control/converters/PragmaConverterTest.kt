package com.infinum.dbinspector.domain.pragma.control.converters

import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.domain.shared.converters.SortConverter
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SortParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
            factory<Converters.Sort> { mockk<SortConverter>() }
        }
    )

    @Test
    fun `Invoke is not implemented and should throw AbstractMethodError`() {
        val given = mockk<PragmaParameters>()

        val sortConverter: Converters.Sort = get()
        val converter = PragmaConverter(sortConverter)

        assertThrows<NotImplementedError> {
            blockingTest {
                converter.invoke(given)
            }
        }
    }

    @Test
    fun `Version converts to data query with same values`() =
        test {
            val given = mockk<PragmaParameters.Version> {
                every { databasePath } returns "test.db"
                every { statement } returns "PRAGMA version()"
                every { connection } returns null
                every { page } returns null
            }
            val expected = Query(
                databasePath = "test.db",
                statement = "PRAGMA version()"
            )

            val sortConverter: Converters.Sort = get()
            val converter = PragmaConverter(sortConverter)

            coEvery { sortConverter.invoke(any()) } returns mockk()

            val actual = converter version given

            coVerify(exactly = 0) { sortConverter.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Pragma converts to data query with same values`() =
        test {
            val given = mockk<PragmaParameters.Pragma> {
                every { databasePath } returns "test.db"
                every { statement } returns "PRAGMA indexes()"
                every { connection } returns null
                every { page } returns Domain.Constants.Limits.INITIAL_PAGE
                every { pageSize } returns Domain.Constants.Limits.PAGE_SIZE
                every { sort } returns SortParameters()
            }
            val expected = Query(
                databasePath = "test.db",
                statement = "PRAGMA indexes()",
                order = Order.ASCENDING
            )

            val sortConverter: Converters.Sort = get()
            val converter = PragmaConverter(sortConverter)

            coEvery { sortConverter.invoke(any()) } returns expected.order

            val actual = converter pragma given

            coVerify(exactly = 1) { sortConverter.invoke(any()) }
            assertEquals(expected, actual)
        }
}
