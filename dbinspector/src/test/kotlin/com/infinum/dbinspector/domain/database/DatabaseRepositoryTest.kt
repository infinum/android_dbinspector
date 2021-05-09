package com.infinum.dbinspector.domain.database

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

@DisplayName("SettingsRepository tests")
internal class DatabaseRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Interactors.GetDatabases>() }
            single { mockk<Interactors.ImportDatabases>() }
            single { mockk<Interactors.RemoveDatabase>() }
            single { mockk<Interactors.RenameDatabase>() }
            single { mockk<Interactors.CopyDatabase>() }
            single { mockk<Control.Database>() }
            factory<Repositories.Database> { DatabaseRepository(get(), get(), get(), get(), get(), get()) }
        }
    )

    @Test
    fun `Get databases calls GetDatabases interactor and Database control`() {
        val interactor: Interactors.GetDatabases = get()
        val control: Control.Database = get()
        val repository: Repositories.Database = get()

        coEvery { interactor.invoke(any()) } returns listOf(mockk())
        coEvery { control.converter get any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.getPage(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter get any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Import databases calls ImportDatabases interactor and Database control`() {
        val interactor: Interactors.ImportDatabases = get()
        val control: Control.Database = get()
        val repository: Repositories.Database = get()

        coEvery { interactor.invoke(any()) } returns listOf(mockk())
        coEvery { control.converter import any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.import(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter import any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Rename database calls RenameDatabase interactor and Database control`() {
        val interactor: Interactors.RenameDatabase = get()
        val control: Control.Database = get()
        val repository: Repositories.Database = get()

        coEvery { interactor.invoke(any()) } returns listOf(mockk())
        coEvery { control.converter rename any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.rename(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter rename any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Remove database calls RemoveDatabase interactor and Database control`() {
        val interactor: Interactors.RemoveDatabase = get()
        val control: Control.Database = get()
        val repository: Repositories.Database = get()

        coEvery { interactor.invoke(any()) } returns listOf(mockk())
        coEvery { control.converter command any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.remove(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter command any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Copy database calls CopyDatabase interactor and Database control`() {
        val interactor: Interactors.CopyDatabase = get()
        val control: Control.Database = get()
        val repository: Repositories.Database = get()

        coEvery { interactor.invoke(any()) } returns listOf(mockk())
        coEvery { control.converter command any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        launch {
            repository.copy(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter command any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }
}
