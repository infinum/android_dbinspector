package com.infinum.dbinspector.domain.pragma.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("GetTablePragmaUseCase tests")
internal class GetTablePragmaUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Repositories.Connection>() }
            factory { mockk<Repositories.Pragma>() }
        }
    )

    @Test
    fun `Invoking use case invokes connection open and pragma table info`() {
        val given = PragmaParameters.Pragma(
            databasePath = "test.db",
            statement = "my_statement"
        )

        val connectionRepository: Repositories.Connection = get()
        val pragmaRepository: Repositories.Pragma = get()
        val useCase = GetTablePragmaUseCase(connectionRepository, pragmaRepository)

        coEvery { connectionRepository.open(any()) } returns mockk()
        coEvery { pragmaRepository.getTableInfo(any()) } returns mockk()

        launch {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { connectionRepository.open(any()) }
        coVerify(exactly = 1) { pragmaRepository.getTableInfo(any()) }
    }
}
