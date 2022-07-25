package com.infinum.dbinspector.domain.pragma

import com.infinum.dbinspector.data.Interactors
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.mockito.kotlin.any

@DisplayName("PragmaRepository tests")
internal class PragmaRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetUserVersion>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetTableInfo>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetForeignKeys>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetIndexes>() }
            factory { mockk<com.infinum.dbinspector.domain.Control.Pragma>() }
        }
    )

    @Test
    fun `Get user version calls interactor and control once`() {
        val interactor: com.infinum.dbinspector.data.Interactors.GetUserVersion = get()
        val control: com.infinum.dbinspector.domain.Control.Pragma = get()
        val repository = PragmaRepository(
            interactor,
            get(),
            get(),
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter version any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        test {
            repository.getUserVersion(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter version any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Get table info calls interactor and control once`() {
        val interactor: com.infinum.dbinspector.data.Interactors.GetTableInfo = get()
        val control: com.infinum.dbinspector.domain.Control.Pragma = get()
        val repository = PragmaRepository(
            get(),
            interactor,
            get(),
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter pragma any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        test {
            repository.getTableInfo(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter pragma any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    @Disabled("This repository call is untestable and needs to be refactored first.")
    fun `Get trigger info uses TriggerInfoColumns enum and Pragma control mapper transformToHeader`() {
        val control: com.infinum.dbinspector.domain.Control.Pragma = get()
        val repository = PragmaRepository(
            get(),
            get(),
            get(),
            get(),
            control
        )

        coEvery { control.converter pragma any() } returns mockk()
        every { control.mapper.transformToHeader() } returns mockk()
        coEvery { repository.getTriggerInfo() } returns mockk {
            every { cells } returns listOf()
        }

        test {
            repository.getTriggerInfo()
        }

        coVerify(exactly = 0) { control.converter pragma any() }
        // verify(exactly = 0) { control.mapper.transformToHeader() }
    }

    @Test
    fun `Get foreign keys calls interactor and control once`() {
        val interactor: com.infinum.dbinspector.data.Interactors.GetForeignKeys = get()
        val control: com.infinum.dbinspector.domain.Control.Pragma = get()
        val repository = PragmaRepository(
            get(),
            get(),
            interactor,
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter pragma any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        test {
            repository.getForeignKeys(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter pragma any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Get indexes calls interactor and control once`() {
        val interactor: com.infinum.dbinspector.data.Interactors.GetIndexes = get()
        val control: com.infinum.dbinspector.domain.Control.Pragma = get()
        val repository = PragmaRepository(
            get(),
            get(),
            get(),
            interactor,
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter pragma any() } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        test {
            repository.getIndexes(any())
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter pragma any() }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }
}
