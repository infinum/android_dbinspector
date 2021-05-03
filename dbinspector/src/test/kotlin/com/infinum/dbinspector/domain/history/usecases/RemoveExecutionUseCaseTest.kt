package com.infinum.dbinspector.domain.history.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("RemoveExecutionUseCase tests")
internal class RemoveExecutionUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Repositories.History>() }
            factory<UseCases.RemoveExecution> { RemoveExecutionUseCase(get()) }
        }
    )

    @Test
    fun `Invoking use case removes execution from history`() {
        val given: HistoryParameters.Execution = HistoryParameters.Execution(
            databasePath = "test.db",
            statement = "SELECT * FROM my_table",
            timestamp = 100L,
            isSuccess = true
        )

        val useCase: UseCases.RemoveExecution = get()
        val historyRepository: Repositories.History = get()

        coEvery { useCase.invoke(given) } returns mockk()
        coEvery { historyRepository.removeExecution(any()) } returns mockk()

        launch {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { historyRepository.removeExecution(any()) }
    }
}
