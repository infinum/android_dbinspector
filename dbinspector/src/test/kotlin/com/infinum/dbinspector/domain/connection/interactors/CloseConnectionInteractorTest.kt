package com.infinum.dbinspector.domain.connection.interactors

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

@DisplayName("CloseConnectionInteractor tests")
internal class CloseConnectionInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.data.Sources.Memory.Connection>() }
        }
    )

    @Test
    fun `Invoking interactor invokes source closeConnection`() {
        val source: com.infinum.dbinspector.data.Sources.Memory.Connection = get()
        val interactor =
            com.infinum.dbinspector.data.interactors.connection.CloseConnectionInteractor(source)

        coEvery { source.closeConnection(any()) } returns mockk()

        test {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.closeConnection(any()) }
    }
}
