package com.infinum.dbinspector.domain.connection.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
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

@DisplayName("OpenConnectionUseCase tests")
internal class OpenConnectionUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Repositories.Connection>() }
            factory<UseCases.OpenConnection> { OpenConnectionUseCase(get()) }
        }
    )

    @Test
    fun `Invoking use case invokes connection open`() {
        val useCase: UseCases.OpenConnection = get()
        val connectionRepository: Repositories.Connection = get()

        coEvery { useCase.invoke(any()) } returns Unit
        coEvery { connectionRepository.open(any()) } returns mockk()

        launch {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { connectionRepository.open(any()) }
    }
}
