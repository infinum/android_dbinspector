package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.FieldType
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.cursor.output.Row
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.Cell
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.shared.BaseTest
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
            single<Mappers.TruncateMode> { TruncateModeMapper() }
            single<Mappers.Cell> { CellMapper(get()) }
            factory<Mappers.Content> { ContentMapper(get()) }
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

            val mapper: Mappers.Content = get()

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
                                FieldType.INTEGER,
                                text = "1"
                            )
                        )
                    )
                )
            )
            val expected = Page(
                cells = listOf(
                    Cell(
                        text = "1"
                    )
                )
            )

            val mapper: Mappers.Content = get()

            val actual = test {
                mapper(given)
            }
            assertEquals(expected, actual)
        }
}
