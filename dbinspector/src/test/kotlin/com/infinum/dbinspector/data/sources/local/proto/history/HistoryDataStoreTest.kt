package com.infinum.dbinspector.data.sources.local.proto.history

import androidx.datastore.core.DataStore
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.firstOrNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("HistoryDataStore tests")
internal class HistoryDataStoreTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<DataStore<HistoryEntity>>() }
        }
    )

    @Test
    fun `Data source exposes datastore`() {
        blockingTest {
            val store: DataStore<HistoryEntity> = get()
            val source = HistoryDataStore(store)

            val actual = source.store()

            assertEquals(store, actual)
        }
    }

    @Test
    @Disabled("Cannot test Flow collect internals")
    fun `No datastore entry returns default entity`() {
        val expected = HistoryEntity.getDefaultInstance()

        val store: DataStore<HistoryEntity> = get()
        val source = HistoryDataStore(store)

        coEvery { store.data } returns mockk {
//            coEvery { collect(any()) } returns Unit
            coEvery { firstOrNull() } returns null
        }

        blockingTest {
            val actual = source.current()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `Data source exposes datastore as Flow`() {
        val store: DataStore<HistoryEntity> = get()
        val source = HistoryDataStore(store)

        every { store.data } returns mockk()

        source.flow()

        verify(exactly = 1) { store.data }
    }
}
