package com.infinum.dbinspector.domain.raw

import com.infinum.dbinspector.data.Interactors
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("RawRepository tests")
internal class RawRepositoryTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetRawQuery>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetRawQueryHeaders>() }
            factory { mockk<com.infinum.dbinspector.data.Interactors.GetAffectedRows>() }
            factory { mockk<com.infinum.dbinspector.domain.Control.Content>() }
        }
    )

    @Test
    fun `Get page calls interactor and control once`() {
        val given: ContentParameters = mockk()
        val interactor: com.infinum.dbinspector.data.Interactors.GetRawQuery = get()
        val control: com.infinum.dbinspector.domain.Control.Content = get()
        val repository = RawRepository(
            interactor,
            get(),
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter.invoke(any()) } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        test {
            repository.getPage(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter.invoke(any()) }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Get headers calls interactor and control once`() {
        val given: ContentParameters = mockk()
        val interactor: com.infinum.dbinspector.data.Interactors.GetRawQueryHeaders = get()
        val control: com.infinum.dbinspector.domain.Control.Content = get()
        val repository = RawRepository(
            get(),
            interactor,
            get(),
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter.invoke(any()) } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()
        coEvery { repository.getHeaders(given) } returns mockk()

        test {
            repository.getHeaders(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter.invoke(any()) }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }

    @Test
    fun `Get affected rows calls interactor and control once`() {
        val given: ContentParameters = mockk()
        val interactor: com.infinum.dbinspector.data.Interactors.GetAffectedRows = get()
        val control: com.infinum.dbinspector.domain.Control.Content = get()
        val repository = RawRepository(
            get(),
            get(),
            interactor,
            control
        )

        coEvery { interactor.invoke(any()) } returns mockk()
        coEvery { control.converter.invoke(any()) } returns mockk()
        coEvery { control.mapper.invoke(any()) } returns mockk()

        test {
            repository.getAffectedRows(given)
        }

        coVerify(exactly = 1) { interactor.invoke(any()) }
        coVerify(exactly = 1) { control.converter.invoke(any()) }
        coVerify(exactly = 1) { control.mapper.invoke(any()) }
    }
}
