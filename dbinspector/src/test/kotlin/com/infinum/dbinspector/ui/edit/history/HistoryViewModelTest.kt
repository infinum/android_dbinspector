package com.infinum.dbinspector.ui.edit.history

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.history.models.History
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("HistoryViewModel tests")
internal class HistoryViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<UseCases.GetHistory>() }
            single { mockk<UseCases.ClearHistory>() }
            single { mockk<UseCases.RemoveExecution>() }
            factory { HistoryViewModel(get(), get(), get()) }
        }
    )

    @Test
    fun `Load history for database path`() {
        val databasePath = "test.db"
        val action: suspend (History) -> Unit = mockk()
        val useCase: UseCases.GetHistory = get()
        val result: Flow<History> = mockk()
        coEvery { useCase.invoke(any()) } returns result

        val viewModel: HistoryViewModel = get()

        viewModel.history(databasePath, action)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

//    @Test
//    fun `Clear history for database path`() {
//        val databasePath = "test.db"
//        val useCase: UseCases.ClearHistory = get()
//        coEvery { useCase.invoke(any()) } returns Unit
//
//        viewModel.clearHistory(databasePath)
//
//        coVerify(exactly = 1) { useCase.invoke(any()) }
//    }
//
//    @Test
//    fun `Remove execution from history`() {
//        val databasePath = "test.db"
//        val execution: Execution = mockk()
//        val useCase: UseCases.RemoveExecution = get()
//        coEvery { useCase.invoke(any()) } returns Unit
//
//        viewModel.clearExecution(databasePath, execution)
//
//        coVerify(exactly = 1) { useCase.invoke(any()) }
//    }
}
