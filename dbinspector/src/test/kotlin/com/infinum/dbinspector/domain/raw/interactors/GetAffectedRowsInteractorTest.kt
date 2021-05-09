package com.infinum.dbinspector.domain.raw.interactors

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

@DisplayName("GetAffectedRowsInteractor tests")
internal class GetAffectedRowsInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Sources.Local.RawQuery>() }
            factory<Interactors.GetAffectedRows> { GetAffectedRowsInteractor(get()) }
        }
    )

    @Test
    fun `Invoking interactor invokes source affectedRows`() {
        val interactor: Interactors.GetAffectedRows = get()
        val source: Sources.Local.RawQuery = get()

        coEvery { source.affectedRows(any()) } returns mockk()

        launch {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.affectedRows(any()) }
    }
}
