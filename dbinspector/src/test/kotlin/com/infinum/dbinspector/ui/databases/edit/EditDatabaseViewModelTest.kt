package com.infinum.dbinspector.ui.databases.edit

import android.content.Context
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.shared.BaseViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.koin.test.inject

@DisplayName("Edit database ViewModel tests")
internal class EditDatabaseViewModelTest : BaseViewModelTest() {

    override val viewModel: EditDatabaseViewModel by inject()

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Context>() }
            single { mockk<UseCases.RenameDatabase>() }
            single { EditDatabaseViewModel(get()) }
        }
    )

    @Test
    fun `Rename database does not change when result list is empty`() {
        val context: Context = get()
        val descriptor: DatabaseDescriptor = mockk()
        val newName = "invoices"
        val action: suspend (DatabaseDescriptor) -> Unit = mockk()
        val useCase: UseCases.RenameDatabase = get()
        val result: List<DatabaseDescriptor> = listOf()
        coEvery { useCase.invoke(any()) } returns result

        viewModel.rename(
            context,
            descriptor,
            newName,
            action
        )

        coVerify(exactly = 1) { useCase.invoke(any()) }
        coVerify(exactly = 0) { action.invoke(descriptor) }
    }

    @Test
    fun `Rename database changes when result list is not empty`() {
        val context: Context = get()
        val descriptor: DatabaseDescriptor = mockk()
        val newName = "invoices"
        val action: suspend (DatabaseDescriptor) -> Unit = mockk()
        val useCase: UseCases.RenameDatabase = get()
        val result: List<DatabaseDescriptor> = listOf(descriptor)
        coEvery { useCase.invoke(any()) } returns result

        viewModel.rename(
            context,
            descriptor,
            newName,
            action
        )

        coVerify(exactly = 1) { useCase.invoke(any()) }
        coVerify(exactly = 1) { action.invoke(descriptor) }
    }
}
