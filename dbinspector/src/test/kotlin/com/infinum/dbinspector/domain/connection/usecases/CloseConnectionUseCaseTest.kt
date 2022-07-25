package com.infinum.dbinspector.domain.connection.usecases

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

@DisplayName("CloseConnectionUseCase tests")
internal class CloseConnectionUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.domain.Repositories.Connection>() }
        }
    )

    @Test
    fun `Invoking use case invokes connection close`() {
        val repository: com.infinum.dbinspector.domain.Repositories.Connection = get()
        val useCase = CloseConnectionUseCase(repository)

        coEvery { repository.close(any()) } returns Unit

        test {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { repository.close(any()) }
    }
}
