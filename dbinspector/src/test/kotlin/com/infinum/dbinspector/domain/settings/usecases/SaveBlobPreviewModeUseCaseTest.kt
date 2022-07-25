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

@DisplayName("SaveBlobPreviewModeUseCase tests")
internal class SaveBlobPreviewModeUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.domain.Repositories.Settings>() }
        }
    )

    @Test
    fun `Invoking use case saves blob preview mode in settings`() {
        val repository: com.infinum.dbinspector.domain.Repositories.Settings = get()
        val useCase = SaveBlobPreviewModeUseCase(repository)

        coEvery { repository.saveBlobPreview(any()) } returns mockk()

        test {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { repository.saveBlobPreview(any()) }
    }
}
