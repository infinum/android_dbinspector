package com.infinum.dbinspector.ui.databases

import android.content.Context
import android.net.Uri
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("DatabaseViewModel tests")
internal class DatabaseViewModelTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Context>() }
            single { mockk<UseCases.GetDatabases>() }
            single { mockk<UseCases.ImportDatabases>() }
            single { mockk<UseCases.RemoveDatabase>() }
            single { mockk<UseCases.CopyDatabase>() }
            factory { DatabaseViewModel(get(), get(), get()) }
        }
    )

    @Test
    fun `Browse and collect all databases`() {
        val context: Context = get()
        val query: String? = null
        val useCase: UseCases.GetDatabases = get()
        val result: List<DatabaseDescriptor> = listOf()
        coEvery { useCase.invoke(any()) } returns result

        val viewModel: DatabaseViewModel = get()

        viewModel.browse(context, query)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Search database by name`() {
        val context: Context = get()
        val query = "artists"
        val useCase: UseCases.GetDatabases = get()
        val result: List<DatabaseDescriptor> = listOf()
        coEvery { useCase.invoke(any()) } returns result

        val viewModel: DatabaseViewModel = get()

        viewModel.browse(context, query)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Import empty list of databases`() {
        val context: Context = get()
        val uris: List<Uri> = listOf()
        val useCase: UseCases.ImportDatabases = get()
        val result: List<DatabaseDescriptor> = listOf()
        coEvery { useCase.invoke(any()) } returns result

        val viewModel: DatabaseViewModel = get()

        viewModel.import(context, uris)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Import a single database`() {
        val context: Context = get()
        val uris: List<Uri> = listOf(mockk())
        val useCase: UseCases.ImportDatabases = get()
        val result: List<DatabaseDescriptor> = listOf()
        coEvery { useCase.invoke(any()) } returns result

        val viewModel: DatabaseViewModel = get()

        viewModel.import(context, uris)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Import multiple databases`() {
        val context: Context = get()
        val uris: List<Uri> = listOf(mockk(), mockk())
        val useCase: UseCases.ImportDatabases = get()
        val result: List<DatabaseDescriptor> = listOf()
        coEvery { useCase.invoke(any()) } returns result

        val viewModel: DatabaseViewModel = get()

        viewModel.import(context, uris)

        coVerify(exactly = 1) { useCase.invoke(any()) }
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
    fun `Copy database`() {
        val context: Context = get()
        val descriptor: DatabaseDescriptor = mockk()
        val useCase: UseCases.CopyDatabase = get()
        val result: List<DatabaseDescriptor> = listOf()
        coEvery { useCase.invoke(any()) } returns result

        val viewModel: DatabaseViewModel = get()

        viewModel.copy(context, descriptor)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }
}
