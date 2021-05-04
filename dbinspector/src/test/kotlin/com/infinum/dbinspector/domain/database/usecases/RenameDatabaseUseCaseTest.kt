package com.infinum.dbinspector.domain.database.usecases

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

@DisplayName("RenameDatabaseUseCase tests")
internal class RenameDatabaseUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Repositories.Database>() }
            factory<UseCases.RenameDatabase> { RenameDatabaseUseCase(get()) }
        }
    )

    @Test
    fun `Invoking use case renames database`() {
        val useCase: UseCases.RenameDatabase = get()
        val databaseRepository: Repositories.Database = get()

        coEvery { useCase.invoke(any()) } returns mockk()
        coEvery { databaseRepository.rename(any()) } returns mockk()

        launch {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { databaseRepository.rename(any()) }
    }
}
