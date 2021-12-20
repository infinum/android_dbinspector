package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("ContentMapper tests")
internal class ContentMapperTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Mappers.Cell> { mockk<CellMapper>() }
        }
    )

    @Test
    fun `Empty local value maps to empty domain value`() =
        launch {
            val given = mockk<QueryResult> {
                every { rows } returns listOf()
                every { nextPage } returns null
                every { beforeCount } returns 0
                every { afterCount } returns 0
            }
            val expected = Page(
                cells = listOf()
            )

            val cellMapper: Mappers.Cell = get()
            val mapper = ContentMapper(cellMapper)

            coEvery { cellMapper.invoke(any()) } returns mockk()
            val actual = test {
                mapper(given)
            }

            coVerify(exactly = 0) { cellMapper.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `QueryResult local value maps to Page with same domain value`() =
        launch {
            val given = mockk<QueryResult> {
                every { rows } returns listOf(
                    mockk {
                        every { position } returns 0
                        every { fields } returns listOf(
                            Field(
                                FieldType.INTEGER,
                                text = "1"
                            )
                        )
                    }
                )
                every { nextPage } returns null
                every { beforeCount } returns 0
                every { afterCount } returns 0
            }
            val expectedCell = Cell(
                text = "1"
            )
            val expected = Page(
                cells = listOf(
                    expectedCell
                )
            )

            val cellMapper: Mappers.Cell = get()
            val mapper = ContentMapper(cellMapper)

            coEvery { cellMapper.invoke(any()) } returns expectedCell
            val actual = test {
                mapper(given)
            }

            coVerify(exactly = 1) { cellMapper.invoke(any()) }
            assertEquals(expected, actual)
        }
}
