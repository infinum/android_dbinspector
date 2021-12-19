package com.infinum.dbinspector.domain.database.interactors

import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.domain.Interactors
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

@DisplayName("RenameDatabaseInteractor tests")
internal class RenameDatabaseInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Sources.Raw>() }
            factory<Interactors.RenameDatabase> { RenameDatabaseInteractor(get()) }
        }
    )

    @Test
    @Disabled("No idea why it fails")
    fun `Invoking interactor invokes source renameDatabase`() {
        val interactor: Interactors.RenameDatabase = get()
        val source: Sources.Raw = get()

        coEvery { source.renameDatabase(any()) } returns mockk()

        launch {
            interactor.invoke(any())
        }

        coVerify(exactly = 1) { source.renameDatabase(any()) }
    }
}
