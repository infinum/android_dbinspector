package com.infinum.dbinspector.domain.settings.usecases

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

@DisplayName("RemoveIgnoredTableNameUseCase tests")
internal class RemoveIgnoredTableNameUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Repositories.Settings>() }
        }
    )

    @Test
    fun `Invoking use case removes ignored table name from settings`() {
        val repository: Repositories.Settings = get()
        val useCase = RemoveIgnoredTableNameUseCase(repository)

        coEvery { repository.removeIgnoredTableName(any()) } returns mockk()

        launch {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { repository.removeIgnoredTableName(any()) }
    }
}
