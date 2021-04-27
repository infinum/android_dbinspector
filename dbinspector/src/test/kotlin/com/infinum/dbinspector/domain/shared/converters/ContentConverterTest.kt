package com.infinum.dbinspector.domain.shared.converters

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.shared.BaseConverterTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

@DisplayName("Content converter tests")
internal class ContentConverterTest : BaseConverterTest() {

    override val converter by inject<Converters.Content>()

    override fun modules(): List<Module> = listOf(
        module {
            single<Converters.Sort> { SortConverter() }
            single<Converters.Content> { ContentConverter(get()) }
        }
    )

    @Test
    fun `Statement converts to data query with same value`() =
        launch {
            val given = ContentParameters(
                statement = "SELECT * FROM users"
            )
            val expected = Query(
                statement = "SELECT * FROM users"
            )
            val actual = test {
                converter(given)
            }
            assertEquals(expected, actual)
        }
}
