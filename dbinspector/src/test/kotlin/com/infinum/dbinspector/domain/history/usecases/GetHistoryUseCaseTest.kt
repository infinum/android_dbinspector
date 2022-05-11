package com.infinum.dbinspector.domain.history.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
            factory { mockk<Repositories.History>() }
        }
    )

    @Test
    fun `Invoking use case gets history per database`() {
        val given = mockk<HistoryParameters.All> {
            every { databasePath } returns "test.db"
        }

        val repository: Repositories.History = get()
        val useCase = GetHistoryUseCase(repository)

        coEvery { repository.getByDatabase(any()) } returns mockk()

        test {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { repository.getByDatabase(any()) }
    }
}
