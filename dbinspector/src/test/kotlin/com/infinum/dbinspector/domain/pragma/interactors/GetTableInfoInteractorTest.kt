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

@DisplayName("GetTableInfoInteractor tests")
internal class GetTableInfoInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Sources.Local.Pragma>() }
            factory<Interactors.GetTableInfo> { GetTableInfoInteractor(get()) }
        }
    )

    @Test
    fun `Invoking interactor invokes source getTableInfo`() {
        val interactor: Interactors.GetTableInfo = get()
        val source: Sources.Local.Pragma = get()

        coEvery { source.getTableInfo(any()) } returns mockk()

        launch {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.getTableInfo(any()) }
    }
}
