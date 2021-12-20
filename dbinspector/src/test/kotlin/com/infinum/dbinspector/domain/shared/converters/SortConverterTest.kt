package com.infinum.dbinspector.domain.shared.converters

import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.domain.shared.models.parameters.SortParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module

@DisplayName("SortConverter tests")
internal class SortConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf()

    @Test
    fun `Sort ASCENDING converts to data entity with same value`() =
        launch {
            val given = mockk<SortParameters> {
                every { sort } returns Sort.ASCENDING
            }
            val expected = Order.ASCENDING

            val converter = SortConverter()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Sort DESCENDING converts to data entity with same value`() =
        launch {
            val given = mockk<SortParameters> {
                every { sort } returns Sort.DESCENDING
            }
            val expected = Order.DESCENDING

            val converter = SortConverter()

            val actual = test {
                converter(given)
            }

            assertEquals(expected, actual)
        }
}
