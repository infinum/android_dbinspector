package com.infinum.dbinspector.domain.raw.interactors

import com.infinum.dbinspector.data.Sources
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

@DisplayName("GetRawQueryInteractor tests")
internal class GetRawQueryInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Sources.Local.RawQuery>() }
        }
    )

    @Test
    fun `Invoking interactor invokes source rawQuery`() {
        val source: Sources.Local.RawQuery = get()
        val interactor = GetRawQueryInteractor(source)

        coEvery { source.rawQuery(any()) } returns mockk()

        test {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.rawQuery(any()) }
    }
}
