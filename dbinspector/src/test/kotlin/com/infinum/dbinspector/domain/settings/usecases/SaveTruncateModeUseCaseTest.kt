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

@DisplayName("SaveTruncateModeUseCase tests")
internal class SaveTruncateModeUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Repositories.Settings>() }
        }
    )

    @Test
    fun `Invoking use case saves truncate mode in settings`() {
        val repository: Repositories.Settings = get()
        val useCase = SaveTruncateModeUseCase(repository)

        coEvery { repository.saveTruncateMode(any()) } returns mockk()

        launch {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { repository.saveTruncateMode(any()) }
    }
}
