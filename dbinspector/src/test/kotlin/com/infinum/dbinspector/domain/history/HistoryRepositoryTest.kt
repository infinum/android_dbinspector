package com.infinum.dbinspector.domain.history

import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("HistoryRepository tests")
internal class HistoryRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Interactors.GetHistory>() }
            single { mockk<Interactors.SaveExecution>() }
            single { mockk<Interactors.ClearHistory>() }
            single { mockk<Interactors.RemoveExecution>() }
            single { mockk<Interactors.GetExecution>() }
            single { mockk<Control.History>() }
            factory<Repositories.History> { HistoryRepository(get(), get(), get(), get(), get(), get()) }
        }
    )

    @Test
    fun `No history by database calls GetHistory interactor and History control converter once`() {
        val given: HistoryParameters.All = mockk {
            every { databasePath } returns "test.db"
        }
        val interactor: Interactors.GetHistory = get()
        val control: Control.History = get()
        val repository: Repositories.History = get()

        every { interactor.invoke(any()) } returns mockk()
        every { control.converter get given } returns mockk()
        coEvery { control.converter.invoke(any()) } throws NotImplementedError()
        coEvery { control.mapper.invoke(any()) } returns mockk()
        every { repository.getByDatabase(given) } returns mockk()

        launch {
            repository.getByDatabase(given)
        }

        verify(exactly = 1) { interactor.invoke(any()) }
        verify(exactly = 1) { control.converter get given }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    @Disabled("Interactor is not called properly")
    fun `Save execution in history calls SaveExecution interactor and History control converter once`() {
        val given: HistoryParameters.Execution = mockk {
            every { databasePath } returns "test.db"
            every { statement } returns "SELECT * FROM my_table"
            every { timestamp } returns 1L
            every { isSuccess } returns true
        }
        val mockHistoryTask: HistoryTask = mockk {
            every { databasePath } returns "test.db"
            every { execution } returns HistoryEntity.ExecutionEntity.getDefaultInstance()
        }
        val interactor: Interactors.SaveExecution = get()
        val control: Control.History = get()
        val repository: Repositories.History = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter execution given } returns mockHistoryTask
        coEvery { control.converter.invoke(any()) } throws NotImplementedError()
        coEvery { control.mapper.invoke(any()) } returns mockk()
        coEvery { repository.saveExecution(given) } returns mockk()

        launch {
            repository.saveExecution(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter execution given }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    @Disabled("Interactor is not called properly")
    fun `Clear history by database calls ClearHistory interactor and History control converter once`() {
        val given: HistoryParameters.All = mockk {
            every { databasePath } returns "test.db"
        }
        val interactor: Interactors.ClearHistory = get()
        val control: Control.History = get()
        val repository: Repositories.History = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter clear given } returns mockk()
        coEvery { control.converter.invoke(any()) } throws NotImplementedError()
        coEvery { control.mapper.invoke(any()) } returns mockk()
        coEvery { repository.clearByDatabase(given) } returns mockk()

        launch {
            repository.clearByDatabase(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter clear given }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    @Disabled("Interactor is not called properly")
    fun `Remove execution in history calls RemoveExecution interactor and History control converter once`() {
        val given: HistoryParameters.Execution = mockk {
            every { databasePath } returns "test.db"
            every { statement } returns "SELECT * FROM my_table"
            every { timestamp } returns 1L
            every { isSuccess } returns true
        }
        val mockHistoryTask: HistoryTask = mockk {
            every { databasePath } returns "test.db"
            every { execution } returns HistoryEntity.ExecutionEntity.getDefaultInstance()
        }
        val interactor: Interactors.RemoveExecution = get()
        val control: Control.History = get()
        val repository: Repositories.History = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter execution given } returns mockHistoryTask
        coEvery { control.converter.invoke(any()) } throws NotImplementedError()
        coEvery { control.mapper.invoke(any()) } returns mockk()
        coEvery { repository.removeExecution(given) } returns mockk()

        launch {
            repository.removeExecution(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter execution given }
        coVerify(exactly = 0) { control.mapper.invoke(any()) }
    }

    @Test
    @Disabled("Interactor is not called properly")
    fun `Get similar executions calls RemoveExecution interactor and History control converter once`() {
        val given: HistoryParameters.Execution = mockk {
            every { databasePath } returns "test.db"
            every { statement } returns "SELECT * FROM my_table"
            every { timestamp } returns 1L
            every { isSuccess } returns true
        }
        val mockHistoryTask: HistoryTask = mockk {
            every { databasePath } returns "test.db"
            every { execution } returns HistoryEntity.ExecutionEntity.getDefaultInstance()
        }
        val interactor: Interactors.GetExecution = get()
        val control: Control.History = get()
        val repository: Repositories.History = get()

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter execution given } returns mockHistoryTask
        coEvery { control.converter.invoke(any()) } throws NotImplementedError()
        coEvery { control.mapper.invoke(any()) } returns mockk()
        coEvery { repository.getSimilarExecution(given) } returns mockk()

        launch {
            repository.getSimilarExecution(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter execution given }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }
}
