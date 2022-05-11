package com.infinum.dbinspector.domain.shared.converters

import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SortParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("ContentConverter tests")
internal class ContentConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Converters.Sort> { mockk<SortConverter>() }
        }
    )

    @Test
    fun `Statement converts to data query with same value`() =
        test {
            val given = mockk<ContentParameters> {
                every { databasePath } returns ""
                every { connection } returns null
                every { pageSize } returns Domain.Constants.Limits.PAGE_SIZE
                every { page } returns Domain.Constants.Limits.INITIAL_PAGE
                every { connection } returns null
                every { statement } returns "SELECT * FROM users"
                every { sort } returns SortParameters()
            }
            val expected = Query(
                statement = "SELECT * FROM users",
                order = Order.ASCENDING
            )

            val sortConverter: Converters.Sort = get()
            val converter = ContentConverter(sortConverter)

            coEvery { sortConverter.invoke(any()) } returns expected.order

            val actual = converter(given)

            coVerify(exactly = 1) { sortConverter.invoke(any()) }
            assertEquals(expected, actual)
        }
}
