package com.infinum.dbinspector.domain.history.control.converters

import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.module.Module

@DisplayName("HistoryConverter tests")
internal class HistoryConverterTest : BaseTest() {

    override fun modules(): List<Module> = listOf()

    @Test
    fun `Invoke is not implemented and should throw AbstractMethodError`() {
        val given = mockk<HistoryParameters>()

        val converter = HistoryConverter()

        assertThrows<NotImplementedError> {
            blockingTest {
                converter.invoke(given)
            }
        }
    }

    @Test
    fun `Get converts to data task with same value`() =
        test {
            val given = mockk<HistoryParameters.All> {
                every { databasePath } returns "test.db"
            }
            val expected = HistoryTask(
                databasePath = "test.db"
            )

            val converter = HistoryConverter()

            val actual = converter get given

            assertEquals(expected, actual)
        }

    @Test
    fun `Execution converts to data task with same values`() =
        test {
            val given = mockk<HistoryParameters.Execution> {
                every { statement } returns "SELECT * FROM users"
                every { databasePath } returns "test.db"
                every { timestamp } returns 1L
                every { isSuccess } returns true
            }
            val expected = HistoryTask(
                execution = HistoryEntity.ExecutionEntity.getDefaultInstance().toBuilder()
                    .setDatabasePath("test.db")
                    .setExecution("SELECT * FROM users")
                    .setTimestamp(1L)
                    .setSuccess(true)
                    .build()
            )

            val converter = HistoryConverter()

            val actual = converter execution given

            assertEquals(expected, actual)
        }

    @Test
    fun `Clear converts to data task with same value`() =
        test {
            val given = mockk<HistoryParameters.All> {
                every { databasePath } returns "test.db"
            }
            val expected = HistoryTask(
                databasePath = "test.db"
            )

            val converter = HistoryConverter()

            val actual = converter clear given

            assertEquals(expected, actual)
        }
}
