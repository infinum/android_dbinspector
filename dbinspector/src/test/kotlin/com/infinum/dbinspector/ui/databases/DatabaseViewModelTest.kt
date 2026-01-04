package com.infinum.dbinspector.ui.databases

import android.content.Context
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("DatabaseViewModel tests")
internal class DatabaseViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Context>() }
            factory { mockk<UseCases.GetDatabases>() }
            factory { mockk<UseCases.ImportDatabases>() }
            factory { mockk<UseCases.RemoveDatabase>() }
            factory { mockk<UseCases.CopyDatabase>() }
        }
    )

    @Test
    fun `Browse and collect all databases`() = test {
        val useCase: UseCases.GetDatabases = get()
        val viewModel = DatabaseViewModel(
            useCase,
            get(),
            get()
        )

        coEvery { useCase.invoke(any()) } returns listOf(
            mockk { every { name } returns "blog" },
            mockk { every { name } returns "chinook" },
            mockk { every { name } returns "northwind" }
        )

        viewModel.browse(get())
        advanceUntilIdle()

        coVerify(exactly = 1) { useCase.invoke(any()) }

        val state = viewModel.stateFlow.filterNotNull().first()
        assertTrue(state is DatabaseState.Databases)
        assertTrue(state.databases.count() == 3)
        assertTrue(state.databases[0].name == "blog")
        assertTrue(state.databases[1].name == "chinook")
        assertTrue(state.databases[2].name == "northwind")

        assertNull(viewModel.errorFlow.value)
    }

    @Test
    fun `Search database by name with result found`() = test {
        val useCase: UseCases.GetDatabases = get()
        val viewModel = DatabaseViewModel(
            useCase,
            get(),
            get()
        )

        coEvery { useCase.invoke(any()) } returns listOf(
            mockk { every { name } returns "blog" }
        )

        viewModel.browse(get(), "log")
        advanceUntilIdle()

        coVerify(exactly = 1) { useCase.invoke(any()) }

        val state = viewModel.stateFlow.filterNotNull().first()
        assertTrue(state is DatabaseState.Databases)
        assertTrue(state.databases.count() == 1)
        assertTrue(state.databases.first().name == "blog")

        assertNull(viewModel.errorFlow.value)
    }

    @Test
    fun `Search database by name without result found`() {
        // Use UnconfinedTestDispatcher to ensure dispatcher is properly initialized
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        try {
            val useCase: UseCases.GetDatabases = get()
            val viewModel = DatabaseViewModel(
                useCase,
                get(),
                get()
            )

            coEvery { useCase.invoke(any()) } returns listOf()

            viewModel.browse(get(), "south")

            coVerify(exactly = 1) { useCase.invoke(any()) }

            blockingTest {
                val state = viewModel.stateFlow.filterNotNull().first()
                assertTrue(state is DatabaseState.Databases)
                assertTrue(state.databases.isEmpty())

                assertNull(viewModel.errorFlow.value)
            }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `Import empty list of databases`() = test {
        val getUseCase: UseCases.GetDatabases = get()
        val importUseCase: UseCases.ImportDatabases = get()

        val viewModel = DatabaseViewModel(
            getUseCase,
            importUseCase,
            get()
        )

        coEvery { getUseCase.invoke(any()) } returns listOf(
            mockk { every { name } returns "blog" }
        )
        coEvery { importUseCase.invoke(any()) } returns listOf()

        viewModel.import(get(), mockk())
        advanceUntilIdle()

        coVerify(exactly = 1) { importUseCase.invoke(any()) }
        coVerify(exactly = 1) { getUseCase.invoke(any()) }

        val state = viewModel.stateFlow.filterNotNull().first()
        assertTrue(state is DatabaseState.Databases)
        assertTrue(state.databases.count() == 1)
        assertTrue(state.databases.first().name == "blog")

        assertNull(viewModel.errorFlow.value)
    }

    @Test
    fun `Import a single database`() = test {
        val getUseCase: UseCases.GetDatabases = get()
        val importUseCase: UseCases.ImportDatabases = get()
        val viewModel = DatabaseViewModel(
            getUseCase,
            importUseCase,
            get()
        )

        coEvery { getUseCase.invoke(any()) } returns listOf(
            mockk { every { name } returns "blog" }
        )
        coEvery { importUseCase.invoke(any()) } returns listOf(
            mockk { every { name } returns "blog" }
        )

        viewModel.import(get(), mockk())
        advanceUntilIdle()

        coVerify(exactly = 1) { importUseCase.invoke(any()) }
        coVerify(exactly = 1) { getUseCase.invoke(any()) }

        val state = viewModel.stateFlow.filterNotNull().first()
        assertTrue(state is DatabaseState.Databases)
        assertTrue(state.databases.count() == 1)
        assertTrue(state.databases.first().name == "blog")

        assertNull(viewModel.errorFlow.value)
    }

    @Test
    fun `Import multiple databases`() = test {
        val getUseCase: UseCases.GetDatabases = get()
        val importUseCase: UseCases.ImportDatabases = get()
        val viewModel = DatabaseViewModel(
            getUseCase,
            importUseCase,
            get()
        )

        coEvery { getUseCase.invoke(any()) } returns listOf(
            mockk { every { name } returns "blog" },
            mockk { every { name } returns "chinook" },
            mockk { every { name } returns "northwind" }
        )
        coEvery { importUseCase.invoke(any()) } returns listOf(
            mockk { every { name } returns "chinook" },
            mockk { every { name } returns "northwind" }
        )

        viewModel.import(get(), mockk())
        advanceUntilIdle()

        coVerify(exactly = 1) { importUseCase.invoke(any()) }
        coVerify(exactly = 1) { getUseCase.invoke(any()) }

        val state = viewModel.stateFlow.filterNotNull().first()
        assertTrue(state is DatabaseState.Databases)
        assertTrue(state.databases.count() == 3)
        assertTrue(state.databases[0].name == "blog")
        assertTrue(state.databases[1].name == "chinook")
        assertTrue(state.databases[2].name == "northwind")

        assertNull(viewModel.errorFlow.value)
    }

//    @Test
//    fun `Remove database`() {
//        val context: Context = get()
//        val descriptor: DatabaseDescriptor = mockk()
//        val useCase: UseCases.RemoveDatabase = get()
//        val result: List<DatabaseDescriptor> = listOf()
//        coEvery { useCase.invoke(any()) } returns result
//
//        val viewModel: DatabaseViewModel = get()
//
//        viewModel.remove(context, descriptor)
//
//        coVerify(exactly = 1) { useCase.invoke(any()) }
//    }

    @Test
    fun `Copy database successful`() {
        // Use UnconfinedTestDispatcher for this test to handle nested launches
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        try {
            val getUseCase: UseCases.GetDatabases = get()
            val copyUseCase: UseCases.CopyDatabase = get()
            val viewModel = DatabaseViewModel(
                getUseCase,
                get(),
                copyUseCase
            )

            coEvery { getUseCase.invoke(any()) } returns listOf(
                mockk { every { name } returns "blog" },
                mockk { every { name } returns "blog_1" }
            )
            coEvery { copyUseCase.invoke(any()) } returns listOf(
                mockk { every { name } returns "blog_1" }
            )

            viewModel.copy(get(), mockk())

            coVerify(exactly = 1) { copyUseCase.invoke(any()) }
            coVerify(exactly = 1) { getUseCase.invoke(any()) }

            blockingTest {
                val state = viewModel.stateFlow.filterNotNull().first()
                assertTrue(state is DatabaseState.Databases)
                assertTrue(state.databases.count() == 2)
                assertTrue(state.databases[0].name == "blog")
                assertTrue(state.databases[1].name == "blog_1")

                assertNull(viewModel.errorFlow.value)
            }
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `Copy database failed`() {
        // Use UnconfinedTestDispatcher for this test to handle nested launches
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        try {
            val getUseCase: UseCases.GetDatabases = get()
            val copyUseCase: UseCases.CopyDatabase = get()
            val viewModel = DatabaseViewModel(
                getUseCase,
                get(),
                copyUseCase
            )

            coEvery { copyUseCase.invoke(any()) } returns listOf()

            viewModel.copy(get(), mockk())

            coVerify(exactly = 1) { copyUseCase.invoke(any()) }
            coVerify(exactly = 0) { getUseCase.invoke(any()) }

            assertNull(viewModel.stateFlow.value)

            blockingTest {
                val error = viewModel.errorFlow.filterNotNull().first()
                assertNotNull(error.message)
                assertTrue(error.stackTrace.isNotEmpty())
            }
        } finally {
            Dispatchers.resetMain()
        }
    }
}
