package com.infinum.dbinspector.domain.schema.trigger.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("GetTriggersUseCase tests")
internal class GetTriggersUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.domain.Repositories.Schema>() }
            factory { mockk<com.infinum.dbinspector.domain.Repositories.Connection>() }
        }
    )

    @Test
    fun `Invoking use case invokes connection open and triggers schema`() {
        val given = ContentParameters(
            databasePath = "test.db",
            statement = "SELECT * FROM tables"
        )

        val connectionRepository: com.infinum.dbinspector.domain.Repositories.Connection = get()
        val schemaRepository: com.infinum.dbinspector.domain.Repositories.Schema = get()
        val useCase = GetTriggersUseCase(connectionRepository, schemaRepository)

        coEvery { connectionRepository.open(any()) } returns mockk()
        coEvery { schemaRepository.getPage(any()) } returns mockk()

        test {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { connectionRepository.open(any()) }
        coVerify(exactly = 1) { schemaRepository.getPage(any()) }
    }
}
