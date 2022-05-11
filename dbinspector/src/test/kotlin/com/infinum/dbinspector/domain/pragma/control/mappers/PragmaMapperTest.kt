package com.infinum.dbinspector.domain.pragma.control.mappers

import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module

@DisplayName("PragmaMapper tests")
internal class PragmaMapperTest : BaseTest() {

    override fun modules(): List<Module> = listOf()

    @Test
    fun `Empty local value maps to empty domain value`() =
        test {
            val given = mockk<QueryResult> {
                every { rows } returns listOf()
                every { nextPage } returns null
                every { beforeCount } returns 0
                every { afterCount } returns 0
            }
            val expected = Page(
                cells = listOf()
            )

            val mapper = PragmaMapper()

            val actual = mapper(given)

            assertEquals(expected, actual)
        }

    @Test
    fun `QueryResult local value maps to Page with same domain value`() =
        test {
            val given = mockk<QueryResult> {
                every { rows } returns listOf(
                    mockk {
                        every { position } returns 0
                        every { fields } returns listOf(
                            Field(
                                FieldType.STRING,
                                text = "column_name"
                            )
                        )
                    }
                )
                every { nextPage } returns null
                every { beforeCount } returns 0
                every { afterCount } returns 0
            }
            val expected = Page(
                cells = listOf(
                    Cell(
                        text = "column_name"
                    )
                )
            )

            val mapper = PragmaMapper()

            val actual = mapper(given)

            assertEquals(expected, actual)
            assertTrue(actual.cells.size == 1)
            assertNotNull(actual.cells.first().text)
            assertNull(actual.cells.first().data)
        }
}
