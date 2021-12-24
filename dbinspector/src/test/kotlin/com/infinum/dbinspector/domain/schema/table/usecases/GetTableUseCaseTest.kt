package com.infinum.dbinspector.domain.schema.table.usecases

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

@DisplayName("GetTableUseCase tests")
internal class GetTableUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Repositories.Schema>() }
            factory { mockk<Repositories.Connection>() }
        }
    )

    @Test
    fun `Invoking use case invokes connection open and gets table content`() {
        val given = ContentParameters(
            databasePath = "test.db",
            statement = "SELECT * FROM tables"
        )

        val connectionRepository: Repositories.Connection = get()
        val schemaRepository: Repositories.Schema = get()
        val useCase = GetTableUseCase(connectionRepository, schemaRepository)

        coEvery { connectionRepository.open(any()) } returns mockk()
        coEvery { schemaRepository.getByName(any()) } returns mockk()

        launch {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { connectionRepository.open(any()) }
        coVerify(exactly = 1) { schemaRepository.getByName(any()) }
    }
}
