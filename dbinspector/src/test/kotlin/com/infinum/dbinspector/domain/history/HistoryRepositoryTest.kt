package com.infinum.dbinspector.domain.history

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("HistoryRepository tests")
internal class HistoryRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Interactors.GetHistory>() }
            factory { mockk<Interactors.SaveExecution>() }
            factory { mockk<Interactors.ClearHistory>() }
            factory { mockk<Interactors.RemoveExecution>() }
            factory { mockk<Interactors.GetExecution>() }
            factory { mockk<Control.History>() }
        }
    )

    @Test
    fun `No history by database calls interactor and control converter once`() {
        val given: HistoryParameters.All = mockk {
            every { databasePath } returns "test.db"
        }
        val interactor: Interactors.GetHistory = get()
        val control: Control.History = get()
        val repository = HistoryRepository(
            interactor,
            get(),
            get(),
            get(),
            get(),
            control
        )

        every { interactor.invoke(any()) } returns mockk()
        every { control.converter get given } returns mockk()
        coEvery { control.converter.invoke(any()) } throws NotImplementedError()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.getByDatabase(given)
        }

        verify(exactly = 1) { interactor.invoke(any()) }
        verify(exactly = 1) { control.converter get given }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Save execution in history calls interactor and control converter once`() {
        val given: HistoryParameters.Execution = mockk {
            every { databasePath } returns "test.db"
            every { statement } returns "SELECT * FROM my_table"
            every { timestamp } returns 1L
            every { isSuccess } returns true
        }
        val interactor: Interactors.SaveExecution = get()
        val control: Control.History = get()
        val repository = HistoryRepository(
            get(),
            interactor,
            get(),
            get(),
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter execution given } returns mockk()
        coEvery { control.converter.invoke(any()) } throws NotImplementedError()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.saveExecution(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter execution given }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Clear history by database calls interactor and control converter once`() {
        val given: HistoryParameters.All = mockk {
            every { databasePath } returns "test.db"
        }
        val interactor: Interactors.ClearHistory = get()
        val control: Control.History = get()
        val repository = HistoryRepository(
            get(),
            get(),
            interactor,
            get(),
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter clear given } returns mockk()
        coEvery { control.converter.invoke(any()) } throws NotImplementedError()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.clearByDatabase(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter clear given }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Remove execution in history calls interactor and control converter once`() {
        val given: HistoryParameters.Execution = mockk {
            every { databasePath } returns "test.db"
            every { statement } returns "SELECT * FROM my_table"
            every { timestamp } returns 1L
            every { isSuccess } returns true
        }
        val interactor: Interactors.RemoveExecution = get()
        val control: Control.History = get()
        val repository = HistoryRepository(
            get(),
            get(),
            get(),
            interactor,
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter execution given } returns mockk()
        coEvery { control.converter.invoke(any()) } throws NotImplementedError()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.removeExecution(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter execution given }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Get similar executions calls interactor and control once`() {
        val given: HistoryParameters.Execution = mockk {
            every { databasePath } returns "test.db"
            every { statement } returns "SELECT * FROM my_table"
            every { timestamp } returns 1L
            every { isSuccess } returns true
        }
        val interactor: Interactors.GetExecution = get()
        val control: Control.History = get()
        val repository = HistoryRepository(
            get(),
            get(),
            get(),
            get(),
            interactor,
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter execution given } returns mockk()
        coEvery { control.converter.invoke(any()) } throws NotImplementedError()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.getSimilarExecution(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter execution given }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }
}
