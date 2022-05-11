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

@DisplayName("GetForeignKeysUseCase tests")
internal class GetForeignKeysUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Repositories.Connection>() }
            factory { mockk<Repositories.Pragma>() }
        }
    )

    @Test
    fun `Invoking use case invokes connection open and pragma foreign keys`() {
        val given: PragmaParameters.Pragma = PragmaParameters.Pragma(
            databasePath = "test.db",
            statement = "my_statement"
        )

        val connectionRepository: Repositories.Connection = get()
        val pragmaRepository: Repositories.Pragma = get()
        val useCase = GetForeignKeysUseCase(connectionRepository, pragmaRepository)

        coEvery { connectionRepository.open(any()) } returns mockk()
        coEvery { pragmaRepository.getForeignKeys(any()) } returns mockk()

        test {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { connectionRepository.open(any()) }
        coVerify(exactly = 1) { pragmaRepository.getForeignKeys(any()) }
    }
}
