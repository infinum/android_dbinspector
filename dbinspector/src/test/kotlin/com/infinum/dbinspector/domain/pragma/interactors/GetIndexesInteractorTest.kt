package com.infinum.dbinspector.domain.pragma.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.domain.Interactors
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
            single { mockk<Sources.Local.Pragma>() }
            factory<Interactors.GetIndexes> { GetIndexesInteractor(get()) }
        }
    )

    @Test
    fun `Invoking interactor invokes source getIndexes`() {
        val interactor: Interactors.GetIndexes = get()
        val source: Sources.Local.Pragma = get()

        coEvery { source.getIndexes(any()) } returns mockk()

        launch {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.getIndexes(any()) }
    }
}
