package com.infinum.dbinspector.domain.schema.view.usecases

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

@DisplayName("DropViewUseCase tests")
internal class DropViewUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.domain.Repositories.Schema>() }
            factory { mockk<com.infinum.dbinspector.domain.Repositories.Connection>() }
        }
    )

    @Test
    fun `Invoking use case invokes connection open and drops view`() {
        val given = ContentParameters(
            databasePath = "test.db",
            statement = "SELECT * FROM tables"
        )

        val connectionRepository: com.infinum.dbinspector.domain.Repositories.Connection = get()
        val schemaRepository: com.infinum.dbinspector.domain.Repositories.Schema = get()
        val useCase = DropViewUseCase(connectionRepository, schemaRepository)

        coEvery { connectionRepository.open(any()) } returns mockk()
        coEvery { schemaRepository.dropByName(any()) } returns mockk()

        test {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { connectionRepository.open(any()) }
        coVerify(exactly = 1) { schemaRepository.dropByName(any()) }
    }
}
