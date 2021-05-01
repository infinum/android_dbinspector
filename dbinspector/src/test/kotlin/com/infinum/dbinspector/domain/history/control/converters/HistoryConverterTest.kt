package com.infinum.dbinspector.domain.history.control.converters

import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.shared.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("HistoryConverter tests")
internal class HistoryConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Converters.History> { HistoryConverter() }
        }
    )

    @Test
    fun `Invoke is not implemented and should throw AbstractMethodError`() {
        val given = HistoryParameters.All(
            databasePath = "test.db",
        )

        val converter: Converters.History = get()

        assertThrows<AbstractMethodError> {
            runBlockingTest {
                converter.invoke(given)
            }
        }
    }

    @Test
    fun `Get converts to data task with same value`() =
        launch {
            val given = HistoryParameters.All(
                databasePath = "test.db",
            )
            val expected = HistoryTask(
                databasePath = "test.db"
            )

            val converter: Converters.History = get()

            val actual = test {
                converter get given
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Execution converts to data task with same values`() =
        launch {
            val given = HistoryParameters.Execution(
                statement = "SELECT * FROM users",
                databasePath = "test.db",
                timestamp = 1L,
                isSuccess = true
            )
            val expected = HistoryTask(
                execution = HistoryEntity.ExecutionEntity.getDefaultInstance().toBuilder()
                    .setDatabasePath("test.db")
                    .setExecution("SELECT * FROM users")
                    .setTimestamp(1L)
                    .setSuccess(true)
                    .build()
            )

            val converter: Converters.History = get()

            val actual = test {
                converter execution given
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Clear converts to data task with same value`() =
        launch {
            val given = HistoryParameters.All(
                databasePath = "test.db",
            )
            val expected = HistoryTask(
                databasePath = "test.db"
            )

            val converter: Converters.History = get()

            val actual = test {
                converter clear given
            }

            assertEquals(expected, actual)
        }
}
