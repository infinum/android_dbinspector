package com.infinum.dbinspector.domain.pragma.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Page
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

@DisplayName("GetTriggerInfoUseCase tests")
internal class GetTriggerInfoUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Repositories.Pragma>() }
            factory<UseCases.GetTriggerInfo> { GetTriggerInfoUseCase(get()) }
        }
    )

    @Test
    fun `Invoking use case invokes pragma trigger info`() {
        val given: PragmaParameters.Pragma = PragmaParameters.Pragma(
            databasePath = "test.db",
            statement = "my_statement"
        )

        val expected: Page = mockk()

        val useCase: UseCases.GetTriggerInfo = get()
        val pragmaRepository: Repositories.Pragma = get()

        coEvery { useCase.invoke(given) } returns expected
        coEvery { pragmaRepository.getTriggerInfo() } returns expected

        launch {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { pragmaRepository.getTriggerInfo() }
    }
}
