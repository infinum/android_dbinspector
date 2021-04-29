package com.infinum.dbinspector.ui.databases

import android.content.Context
import android.net.Uri
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject

internal class DatabaseViewModelTest : BaseViewModelTest() {

    override val viewModel: DatabaseViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Context>() }
            single { mockk<UseCases.GetDatabases>() }
            single { mockk<UseCases.ImportDatabases>() }
            single { mockk<UseCases.RemoveDatabase>() }
            single { mockk<UseCases.CopyDatabase>() }
            single { DatabaseViewModel(get(), get(), get(), get()) }
        }
    )

    @Test
    fun `Browse and collect all databases`() {
        val context: Context = get()
        val query: String? = null
        val useCase: UseCases.GetDatabases = get()
        val result: List<DatabaseDescriptor> = listOf()
        coEvery { useCase.invoke(any()) } returns result

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

        viewModel.import(context, uris)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Remove database`() {
        val context: Context = get()
        val descriptor: DatabaseDescriptor = mockk()
        val useCase: UseCases.RemoveDatabase = get()
        val result: List<DatabaseDescriptor> = listOf()
        coEvery { useCase.invoke(any()) } returns result

        viewModel.remove(context, descriptor)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }

    @Test
    fun `Copy database`() {
        val context: Context = get()
        val descriptor: DatabaseDescriptor = mockk()
        val useCase: UseCases.CopyDatabase = get()
        val result: List<DatabaseDescriptor> = listOf()
        coEvery { useCase.invoke(any()) } returns result

        viewModel.copy(context, descriptor)

        coVerify(exactly = 1) { useCase.invoke(any()) }
    }
}
