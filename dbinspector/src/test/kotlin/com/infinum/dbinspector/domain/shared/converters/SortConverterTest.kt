package com.infinum.dbinspector.domain.shared.converters

import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.parameters.SortParameters
import com.infinum.dbinspector.shared.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("SortConverter tests")
internal class SortConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Converters.Sort> { SortConverter() }
        }
    )

    @Test
    fun `Sort ASCENDING converts to data entity with same value`() =
        launch {
            val given = SortParameters(
                sort = Sort.ASCENDING
            )
            val expected = Order.ASCENDING

            val converter: Converters.Sort = get()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Sort DESCENDING converts to data entity with same value`() =
        launch {
            val given = SortParameters(
                sort = Sort.DESCENDING
            )
            val expected = Order.DESCENDING

            val converter: Converters.Sort = get()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }
}
