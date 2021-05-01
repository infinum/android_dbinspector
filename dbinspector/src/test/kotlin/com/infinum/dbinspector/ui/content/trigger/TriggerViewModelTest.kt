package com.infinum.dbinspector.ui.content.trigger

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Sort
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("TriggerViewModel tests")
internal class TriggerViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.OpenConnection>() }
            single { mockk<UseCases.CloseConnection>() }
            single { mockk<UseCases.GetTriggerInfo>() }
            single { mockk<UseCases.GetTrigger>() }
            single { mockk<UseCases.DropTrigger>() }
            factory { TriggerViewModel(get(), get(), get(), get(), get()) }
        }
    )

    @Test
    fun `Get trigger header`() {
        val given = ""
        val expected = ""

        val viewModel: TriggerViewModel = get()
        val actual = viewModel.headerStatement(given)

        assertTrue(actual.isBlank())
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @EnumSource(Sort::class)
    fun `Get trigger per sort`(sort: Sort) {
        val given = "my_trigger"
        val expected = "SELECT name, sql FROM \"sqlite_master\" WHERE (type = 'trigger' AND name = '$given') LIMIT 1"

        val viewModel: TriggerViewModel = get()
        val actual = viewModel.schemaStatement(given, null, sort)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }

    @Test
    fun `Drop trigger`() {
        val given = "my_trigger"
        val expected = "DROP TRIGGER \"$given\""

        val viewModel: TriggerViewModel = get()
        val actual = viewModel.dropStatement(given)

        assertTrue(actual.isNotBlank())
        assertEquals(expected, actual)
    }
}
