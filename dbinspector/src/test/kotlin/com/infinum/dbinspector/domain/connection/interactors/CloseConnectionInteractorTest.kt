package com.infinum.dbinspector.domain.connection.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.domain.Interactors
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
            single { mockk<Sources.Memory>() }
            factory<Interactors.CloseConnection> { CloseConnectionInteractor(get()) }
        }
    )

    @Test
    fun `Invoking interactor invokes source closeConnection`() {
        val interactor: Interactors.CloseConnection = get()
        val source: Sources.Memory = get()

        coEvery { source.closeConnection(any()) } returns mockk()

        launch {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.closeConnection(any()) }
    }
}
