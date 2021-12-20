package com.infinum.dbinspector.domain.history.control.mappers

import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.history.models.Execution
import com.infinum.dbinspector.domain.history.models.History
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("HistoryMapper tests")
internal class HistoryMapperTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory<Mappers.Execution> { mockk<ExecutionMapper>() }
        }
    )

    @Test
    fun `Empty local values maps to empty domain values`() =
        launch {
            val given = HistoryEntity.getDefaultInstance()

            val expected = History()

            val executionMapper: Mappers.Execution = get()
            val mapper = HistoryMapper(executionMapper)

            coEvery { executionMapper.invoke(any()) } returns mockk()
            val actual = test {
                mapper(given)
            }

            coVerify(exactly = 0) { executionMapper.invoke(any()) }
            assertEquals(expected, actual)
        }

    @Test
    fun `Specific Execution local values map to same domain values`() =
        launch {
            val given = HistoryEntity.newBuilder().addExecutions(
                HistoryEntity.ExecutionEntity.newBuilder()
                    .setExecution("SELECT * FROM users")
                    .setTimestamp(1L)
                    .setSuccess(true)
                    .build()
            ).build()

            val expectedExecution = Execution(
                statement = "SELECT * FROM users",
                timestamp = 1L,
                isSuccessful = true
            )
            val expected = History(
                executions = listOf(
                    expectedExecution
                )
            )

            val executionMapper: Mappers.Execution = get()
            val mapper = HistoryMapper(executionMapper)

            coEvery { executionMapper.invoke(any()) } returns expectedExecution
            val actual = test {
                mapper(given)
            }

            coVerify(exactly = 1) { executionMapper.invoke(any()) }
            assertEquals(expected, actual)
        }
}
