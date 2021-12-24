package com.infinum.dbinspector.domain.pragma.usecases

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

@DisplayName("GetTriggerInfoUseCase tests")
internal class GetTriggerInfoUseCaseTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Repositories.Pragma>() }
        }
    )

    @Test
    fun `Invoking use case invokes pragma trigger info`() {
        val repository: Repositories.Pragma = get()
        val useCase = GetTriggerInfoUseCase(repository)

        coEvery { repository.getTriggerInfo() } returns mockk()

        launch {
            useCase.invoke(any())
        }

        coVerify(exactly = 1) { repository.getTriggerInfo() }
    }
}
