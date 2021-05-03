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

@DisplayName("GetHistoryUseCase tests")
internal class GetHistoryUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Repositories.History>() }
            factory<UseCases.GetHistory> { GetHistoryUseCase(get()) }
        }
    )

    @Test
    fun `Invoking use case gets history per database`() {
        val given: HistoryParameters.All = HistoryParameters.All(
            databasePath = "test.db"
        )

        val useCase: UseCases.GetHistory = get()
        val historyRepository: Repositories.History = get()

        coEvery { useCase.invoke(given) } returns mockk()
        coEvery { historyRepository.getByDatabase(any()) } returns mockk()

        launch {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { historyRepository.getByDatabase(any()) }
    }
}
