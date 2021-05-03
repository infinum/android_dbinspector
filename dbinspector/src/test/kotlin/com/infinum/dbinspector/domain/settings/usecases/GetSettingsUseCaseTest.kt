package com.infinum.dbinspector.domain.settings.usecases

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

@DisplayName("GetSettingsUseCase tests")
internal class GetSettingsUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Repositories.Settings>() }
            factory<UseCases.GetSettings> { GetSettingsUseCase(get()) }
        }
    )

    @Test
    fun `Invoking use case gets current settings`() {
        val useCase: UseCases.GetSettings = get()
        val settingsRepository: Repositories.Settings = get()

        coEvery { useCase.invoke(any()) } returns mockk()
        coEvery { settingsRepository.getPage(any()) } returns mockk()

        launch {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { settingsRepository.getPage(any()) }
    }
}
