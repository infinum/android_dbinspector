package com.infinum.dbinspector.domain.connection

import com.infinum.dbinspector.data.Interactors
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

@DisplayName("ConnectionRepository tests")
internal class ConnectionRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.data.Interactors.OpenConnection>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.CloseConnection>() }
            factory { mockk<com.infinum.dbinspector.domain.Control.Connection>() }
        }
    )

    @Test
    fun `Connection repository open calls open interactor`() {
        val interactor: com.infinum.dbinspector.data.Interactors.OpenConnection = get()
        val control: com.infinum.dbinspector.domain.Control.Connection = get()
        val repository = ConnectionRepository(
            interactor,
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()
        coEvery { control.converter.invoke(any()) } returns ""

        test {
            repository.open(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
        coVerify(exactly = 1) { control.converter.invoke(any()) }
    }

    @Test
    fun `Connection repository close calls close interactor`() {
        val interactor: com.infinum.dbinspector.data.Interactors.CloseConnection = get()
        val control: com.infinum.dbinspector.domain.Control.Connection = get()
        val repository = ConnectionRepository(
            get(),
            interactor,
            control
        )

        coEvery { interactor.invoke(any()) } returns Unit
        coEvery { control.mapper.invoke(any()) } returns mockk()
        coEvery { control.converter.invoke(any()) } returns ""

        test {
            repository.close(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
        coVerify(exactly = 1) { control.converter.invoke(any()) }
    }
}
