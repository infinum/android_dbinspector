package com.infinum.dbinspector.domain.pragma.control.mappers

import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.cursor.output.Row
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.shared.BaseMapperTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

@DisplayName("Pragma mapper tests")
internal class PragmaMapperTest : BaseMapperTest() {

    override val mapper by inject<Mappers.Pragma>()

    override fun modules(): List<Module> = listOf(
        module {
            single<Mappers.Pragma> { PragmaMapper() }
        }
    )

    @Test
    fun `Empty local value maps to empty domain value`() =
        launch {
            val given = QueryResult(
                rows = listOf()
            )
            val expected = Page(
                cells = listOf()
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }

    @Test
    fun `QueryResult local value maps to Page with same domain value`() =
        launch {
            val given = QueryResult(
                rows = listOf(
                    Row(
                        position = 0,
                        fields = listOf(
                            Field(
                                FieldType.STRING,
                                text = "column_name"
                            )
                        )
                    )
                )
            )
            val expected = Page(
                cells = listOf(
                    Cell(
                        text = "column_name"
                    )
                )
            )
            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
            assertTrue(actual.cells.size == 1)
            assertNotNull(actual.cells.first().text)
            assertNull(actual.cells.first().data)
        }
}
