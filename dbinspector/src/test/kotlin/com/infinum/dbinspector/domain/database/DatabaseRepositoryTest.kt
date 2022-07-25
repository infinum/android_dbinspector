package com.infinum.dbinspector.domain.database

import com.infinum.dbinspector.data.Interactors
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

@DisplayName("DatabaseRepository tests")
internal class DatabaseRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetDatabases>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.ImportDatabases>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.RemoveDatabase>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.RenameDatabase>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.CopyDatabase>() }
            factory { mockk<com.infinum.dbinspector.domain.Control.Database>() }
        }
    )

    @Test
    fun `Get databases calls GetDatabases interactor and Database control`() {
        val interactor: com.infinum.dbinspector.data.Interactors.GetDatabases = get()
        val control: com.infinum.dbinspector.domain.Control.Database = get()
        val repository = DatabaseRepository(
            interactor,
            get(),
            get(),
            get(),
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns listOf(mockk())
        coEvery { control.converter get any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        test {
            repository.getPage(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter get any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Import databases calls ImportDatabases interactor and Database control`() {
        val interactor: com.infinum.dbinspector.data.Interactors.ImportDatabases = get()
        val control: com.infinum.dbinspector.domain.Control.Database = get()
        val repository = DatabaseRepository(
            get(),
            interactor,
            get(),
            get(),
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns listOf(mockk())
        coEvery { control.converter import any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        test {
            repository.import(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter import any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Rename database calls RenameDatabase interactor and Database control`() {
        val interactor: com.infinum.dbinspector.data.Interactors.RenameDatabase = get()
        val control: com.infinum.dbinspector.domain.Control.Database = get()
        val repository = DatabaseRepository(
            get(),
            get(),
            get(),
            interactor,
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns listOf(mockk())
        coEvery { control.converter rename any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        test {
            repository.rename(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter rename any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Remove database calls RemoveDatabase interactor and Database control`() {
        val interactor: com.infinum.dbinspector.data.Interactors.RemoveDatabase = get()
        val control: com.infinum.dbinspector.domain.Control.Database = get()
        val repository = DatabaseRepository(
            get(),
            get(),
            interactor,
            get(),
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns listOf(mockk())
        coEvery { control.converter command any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        test {
            repository.remove(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter command any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Copy database calls CopyDatabase interactor and Database control`() {
        val interactor: com.infinum.dbinspector.data.Interactors.CopyDatabase = get()
        val control: com.infinum.dbinspector.domain.Control.Database = get()
        val repository = DatabaseRepository(
            get(),
            get(),
            get(),
            get(),
            interactor,
            control
        )

        coEvery { interactor.invoke(any()) } returns listOf(mockk())
        coEvery { control.converter command any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        test {
            repository.copy(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter command any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }
}
