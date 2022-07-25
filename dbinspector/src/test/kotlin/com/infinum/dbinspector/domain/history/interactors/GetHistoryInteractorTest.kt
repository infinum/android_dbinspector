package com.infinum.dbinspector.domain.history.interactors

import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get
import org.mockito.kotlin.any

@DisplayName("GetHistoryInteractor tests")
internal class GetHistoryInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.data.Sources.Local.History>() }
        }
    )

    @Test
    @Disabled("Source is not correct.")
    fun `Invoking interactor invokes source flow`() {
        val source: com.infinum.dbinspector.data.Sources.Local.History = get()
        val interactor =
            com.infinum.dbinspector.data.interactors.history.GetHistoryInteractor(source)

        coEvery { source.flow() } returns mockk()

        test {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.flow() }
    }
}
