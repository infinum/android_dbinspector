package com.infinum.dbinspector.domain.history

import com.infinum.dbinspector.data.Interactors
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
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetHistory>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.SaveExecution>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.ClearHistory>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.RemoveExecution>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetExecution>() }
            factory { mockk<com.infinum.dbinspector.domain.Control.History>() }
        }
    )

    @Test
    fun `No history by database calls interactor and control converter once`() {
        val given: HistoryParameters.All = mockk {
            every { databasePath } returns "test.db"
        }
        val interactor: com.infinum.dbinspector.data.Interactors.GetHistory = get()
        val control: com.infinum.dbinspector.domain.Control.History = get()
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

        test {
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
        val interactor: com.infinum.dbinspector.data.Interactors.SaveExecution = get()
        val control: com.infinum.dbinspector.domain.Control.History = get()
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

        test {
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
        val interactor: com.infinum.dbinspector.data.Interactors.ClearHistory = get()
        val control: com.infinum.dbinspector.domain.Control.History = get()
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

        test {
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
        val interactor: com.infinum.dbinspector.data.Interactors.RemoveExecution = get()
        val control: com.infinum.dbinspector.domain.Control.History = get()
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

        test {
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
        val interactor: com.infinum.dbinspector.data.Interactors.GetExecution = get()
        val control: com.infinum.dbinspector.domain.Control.History = get()
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

        test {
            repository.getSimilarExecution(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter execution given }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }
}
