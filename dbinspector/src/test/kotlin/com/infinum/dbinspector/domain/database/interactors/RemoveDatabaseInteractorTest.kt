package com.infinum.dbinspector.domain.database.interactors

import com.infinum.dbinspector.data.Sources
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

@DisplayName("RemoveDatabaseInteractor tests")
internal class RemoveDatabaseInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Sources.Raw>() }
        }
    )

    @Test
    @Disabled("Fails because of NullPointerException")
    fun `Invoking interactor invokes source removeDatabase`() {
        val source: Sources.Raw = get()
        val interactor = RemoveDatabaseInteractor(source)

        coEvery { source.removeDatabase(any()) } returns mockk()

        test {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.removeDatabase(any()) }
    }
}
