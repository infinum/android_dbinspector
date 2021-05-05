package com.infinum.dbinspector.domain.connection

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
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

@DisplayName("ConnectionRepository tests")
internal class ConnectionRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Interactors.OpenConnection>() }
            single { mockk<Interactors.CloseConnection>() }
            single { mockk<Control.Connection>() }
            factory<Repositories.Connection> { ConnectionRepository(get(), get(), get()) }
        }
    )

    @Test
    fun `Connection repository open calls open interactor`() {
        val repository: Repositories.Connection = get()
        val interactor: Interactors.OpenConnection = get()
        val control: Control.Connection = get()

        coEvery { repository.open(any()) } returns mockk()
        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()
        coEvery { control.converter.invoke(any()) } returns ""

        launch {
            repository.open(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
        coVerify(exactly = 1) { control.converter.invoke(any()) }
    }

    @Test
    fun `Connection repository close calls close interactor`() {
        val repository: Repositories.Connection = get()
        val interactor: Interactors.CloseConnection = get()
        val control: Control.Connection = get()

        coEvery { repository.close(any()) } returns mockk()
        coEvery { interactor.invoke(any()) } returns Unit
        coEvery { control.mapper.invoke(any()) } returns mockk()
        coEvery { control.converter.invoke(any()) } returns ""

        launch {
            repository.close(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
        coVerify(exactly = 1) { control.converter.invoke(any()) }
    }
}
