package com.infinum.dbinspector.domain.pragma.interactors

import com.infinum.dbinspector.data.Data
import com.infinum.dbinspector.data.Sources
import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.data.models.local.cursor.input.Query
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

@DisplayName("GetForeignKeysInteractor tests")
internal class GetForeignKeysInteractorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Sources.Local.Pragma>() }
        }
    )

    @Test
    @Disabled("Fails because of NullPointerException")
    fun `Invoking interactor invokes source getForeignKeys`() {
        val given = mockk<Query> {
            every { databasePath } returns ""
            every { database } returns mockk()
            every { statement } returns ""
            every { order } returns Order.ASCENDING
            every { pageSize } returns Data.Constants.Limits.PAGE_SIZE
            every { page } returns Data.Constants.Limits.INITIAL_PAGE
        }
        val source: Sources.Local.Pragma = get()
        val interactor = GetForeignKeysInteractor(source)

        coEvery { source.getForeignKeys(any()) } returns mockk()

        launch {
            interactor.invoke(given)
        }

        coVerify(exactly = 1) { source.getForeignKeys(any()) }
    }
}
