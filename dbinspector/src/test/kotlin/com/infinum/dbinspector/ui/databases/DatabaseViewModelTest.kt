package com.infinum.dbinspector.ui.databases

import android.content.Context
import app.cash.turbine.test
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.awaitCancellation
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
            factory { mockk<com.infinum.dbinspector.domain.UseCases.GetDatabases>() }
            factory { mockk<com.infinum.dbinspector.domain.UseCases.ImportDatabases>() }
            factory { mockk<com.infinum.dbinspector.domain.UseCases.RemoveDatabase>() }
            factory { mockk<com.infinum.dbinspector.domain.UseCases.CopyDatabase>() }
        }
    )

    @Test
    fun `Browse and collect all databases`() {
        val useCase: com.infinum.dbinspector.domain.UseCases.GetDatabases = get()
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

        coVerify(exactly = 1) { useCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                val item: DatabaseState? = awaitItem()
                assertTrue(item is DatabaseState.Databases)
                assertTrue(item.databases.count() == 3)
                assertTrue(item.databases[0].name == "blog")
                assertTrue(item.databases[1].name == "chinook")
                assertTrue(item.databases[2].name == "northwind")
                awaitCancellation()
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Search database by name with result found`() {
        val useCase: com.infinum.dbinspector.domain.UseCases.GetDatabases = get()
        val viewModel = DatabaseViewModel(
            useCase,
            get(),
            get()
        )

        coEvery { useCase.invoke(any()) } returns listOf(
            mockk { every { name } returns "blog" }
        )

        viewModel.browse(get(), "log")

        coVerify(exactly = 1) { useCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                val item: DatabaseState? = awaitItem()
                assertTrue(item is DatabaseState.Databases)
                assertTrue(item.databases.count() == 1)
                assertTrue(item.databases.first().name == "blog")
                awaitCancellation()
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Search database by name without result found`() {
        val useCase: com.infinum.dbinspector.domain.UseCases.GetDatabases = get()
        val viewModel = DatabaseViewModel(
            useCase,
            get(),
            get()
        )

        coEvery { useCase.invoke(any()) } returns listOf()

        viewModel.browse(get(), "south")

        coVerify(exactly = 1) { useCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                val item: DatabaseState? = awaitItem()
                assertTrue(item is DatabaseState.Databases)
                assertTrue(item.databases.count() == 0)
                assertTrue(item.databases.isEmpty())
                awaitCancellation()
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Import empty list of databases`() {
        val getUseCase: com.infinum.dbinspector.domain.UseCases.GetDatabases = get()
        val importUseCase: com.infinum.dbinspector.domain.UseCases.ImportDatabases = get()

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

        coVerify(exactly = 1) { importUseCase.invoke(any()) }
        coVerify(exactly = 1) { getUseCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                val item: DatabaseState? = awaitItem()
                assertTrue(item is DatabaseState.Databases)
                assertTrue(item.databases.count() == 1)
                assertTrue(item.databases.first().name == "blog")
                awaitCancellation()
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Import a single database`() {
        val getUseCase: com.infinum.dbinspector.domain.UseCases.GetDatabases = get()
        val importUseCase: com.infinum.dbinspector.domain.UseCases.ImportDatabases = get()
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

        coVerify(exactly = 1) { importUseCase.invoke(any()) }
        coVerify(exactly = 1) { getUseCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                val item: DatabaseState? = awaitItem()
                assertTrue(item is DatabaseState.Databases)
                assertTrue(item.databases.count() == 1)
                assertTrue(item.databases.first().name == "blog")
                awaitCancellation()
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Import multiple databases`() {
        val getUseCase: com.infinum.dbinspector.domain.UseCases.GetDatabases = get()
        val importUseCase: com.infinum.dbinspector.domain.UseCases.ImportDatabases = get()
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

        coVerify(exactly = 1) { importUseCase.invoke(any()) }
        coVerify(exactly = 1) { getUseCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                val item: DatabaseState? = awaitItem()
                assertTrue(item is DatabaseState.Databases)
                assertTrue(item.databases.count() == 3)
                assertTrue(item.databases[0].name == "blog")
                assertTrue(item.databases[1].name == "chinook")
                assertTrue(item.databases[2].name == "northwind")
                awaitCancellation()
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
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
        val getUseCase: com.infinum.dbinspector.domain.UseCases.GetDatabases = get()
        val copyUseCase: com.infinum.dbinspector.domain.UseCases.CopyDatabase = get()
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
        test {
            viewModel.stateFlow.test {
                val item: DatabaseState? = awaitItem()
                assertTrue(item is DatabaseState.Databases)
                assertTrue(item.databases.count() == 2)
                assertTrue(item.databases[0].name == "blog")
                assertTrue(item.databases[1].name == "blog_1")
                awaitCancellation()
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                expectNoEvents()
            }
        }
    }

    @Test
    fun `Copy database failed`() {
        val getUseCase: com.infinum.dbinspector.domain.UseCases.GetDatabases = get()
        val copyUseCase: com.infinum.dbinspector.domain.UseCases.CopyDatabase = get()
        val viewModel = DatabaseViewModel(
            getUseCase,
            get(),
            copyUseCase
        )

        coEvery { copyUseCase.invoke(any()) } returns listOf()

        viewModel.copy(get(), mockk())

        coVerify(exactly = 1) { copyUseCase.invoke(any()) }
        coVerify(exactly = 0) { getUseCase.invoke(any()) }
        test {
            viewModel.stateFlow.test {
                assertNull(awaitItem())
            }
            viewModel.eventFlow.test {
                expectNoEvents()
            }
            viewModel.errorFlow.test {
                val item: Throwable? = awaitItem()
                assertTrue(item is Throwable)
                assertNull(item.message)
                assertTrue(item.stackTrace.isNotEmpty())
            }
        }
    }
}
