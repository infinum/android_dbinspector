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

@DisplayName("SaveBlobPreviewModeUseCase tests")
internal class SaveBlobPreviewModeUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Repositories.Settings>() }
            factory<UseCases.SaveBlobPreviewMode> { SaveBlobPreviewModeUseCase(get()) }
        }
    )

    @Test
    fun `Invoking use case saves blob preview mode in settings`() {
        val useCase: UseCases.SaveBlobPreviewMode = get()
        val settingsRepository: Repositories.Settings = get()

        coEvery { useCase.invoke(any()) } returns mockk()
        coEvery { settingsRepository.saveBlobPreview(any()) } returns mockk()

        launch {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { settingsRepository.saveBlobPreview(any()) }
    }
}
