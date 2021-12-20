package com.infinum.dbinspector.domain.connection.interactors

import com.infinum.dbinspector.data.Sources
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

@DisplayName("OpenConnectionInteractor tests")
internal class OpenConnectionInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Sources.Memory>() }
        }
    )

    @Test
    fun `Invoking interactor invokes source openConnection`() {
        val source: Sources.Memory = get()
        val interactor = OpenConnectionInteractor(source)

        coEvery { source.openConnection(any()) } returns mockk()

        launch {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.openConnection(any()) }
    }
}
