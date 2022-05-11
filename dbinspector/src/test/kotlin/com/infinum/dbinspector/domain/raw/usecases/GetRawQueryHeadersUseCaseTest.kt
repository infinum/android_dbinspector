package com.infinum.dbinspector.domain.raw.usecases

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

@DisplayName("GetRawQueryHeadersUseCase tests")
internal class GetRawQueryHeadersUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Repositories.RawQuery>() }
            factory { mockk<Repositories.Connection>() }
        }
    )

    @Test
    fun `Invoking use case invokes connection open and table headers`() {
        val given = ContentParameters(
            databasePath = "test.db",
            statement = "SELECT * FROM tables"
        )

        val connectionRepository: Repositories.Connection = get()
        val rawQueryRepository: Repositories.RawQuery = get()
        val useCase = GetRawQueryHeadersUseCase(connectionRepository, rawQueryRepository)

        coEvery { connectionRepository.open(any()) } returns mockk()
        coEvery { rawQueryRepository.getHeaders(any()) } returns mockk()

        test {
            useCase.invoke(given)
        }

        coVerify(exactly = 1) { connectionRepository.open(any()) }
        coVerify(exactly = 1) { rawQueryRepository.getHeaders(any()) }
    }
}
