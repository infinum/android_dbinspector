package com.infinum.dbinspector.domain.database.usecases

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

@DisplayName("RenameDatabaseUseCase tests")
internal class RenameDatabaseUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Repositories.Database>() }
        }
    )

    @Test
    fun `Invoking use case renames database`() {
        val repository: Repositories.Database = get()
        val useCase = RenameDatabaseUseCase(repository)

        coEvery { repository.rename(any()) } returns mockk()

        test {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { repository.rename(any()) }
    }
}
