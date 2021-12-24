package com.infinum.dbinspector.domain.history.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.mockito.kotlin.any

@DisplayName("GetSimilarExecutionUseCase tests")
internal class GetSimilarExecutionUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Repositories.History>() }
        }
    )

    @Test
    fun `Invoking use case gets history of similar executions`() {
        val repository: Repositories.History = get()
        val useCase = GetSimilarExecutionUseCase(repository)

        coEvery { repository.getSimilarExecution(any()) } returns mockk()

        launch {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { repository.getSimilarExecution(any()) }
    }
}
