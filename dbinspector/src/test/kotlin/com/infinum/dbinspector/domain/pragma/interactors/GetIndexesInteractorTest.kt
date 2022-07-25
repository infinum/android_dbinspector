package com.infinum.dbinspector.domain.pragma.interactors

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

@DisplayName("GetIndexesInteractor tests")
internal class GetIndexesInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.data.Sources.Local.Pragma>() }
        }
    )

    @Test
    fun `Invoking interactor invokes source getIndexes`() {
        val source: com.infinum.dbinspector.data.Sources.Local.Pragma = get()
        val interactor =
            com.infinum.dbinspector.data.interactors.pragma.GetIndexesInteractor(source)

        coEvery { source.getIndexes(any()) } returns mockk()

        test {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.getIndexes(any()) }
    }
}
