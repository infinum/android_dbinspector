package com.infinum.dbinspector.data.sources.memory.connection

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("AndroidConnectionSource tests")
internal class AndroidConnectionSourceTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { AndroidConnectionSource() }
        }
    )

    @Test
    fun `New instance has empty but existing connection pool`() {
        val source: AndroidConnectionSource = get()

        assertTrue(source.connectionPool.isEmpty())
        assertNotNull(source.connectionPool)
    }

    @Test
    fun `Open connection adds new key to connection pool`() {
        val given = "test.db"

        mockkStatic(SQLiteDatabase::class)
        every { SQLiteDatabase.openOrCreateDatabase(given, null) } returns mockk {
            every { path } returns given
        }

        val source: AndroidConnectionSource = get()

        launch {
            source.openConnection(given)
        }

        unmockkStatic(SQLiteDatabase::class)

        assertTrue(source.connectionPool.isNotEmpty())
        assertNotNull(source.connectionPool)
        assertEquals(source.connectionPool.size, 1)
        assertTrue(source.connectionPool.containsKey(given))
    }

    @Test
    fun `Open connection reuses existing key from connection pool`() {
        val given = "test.db"

        val expected: SQLiteDatabase = mockk()

        mockkStatic(SQLiteDatabase::class)
        every { SQLiteDatabase.openOrCreateDatabase(given, null) } returns mockk {
            every { path } returns given
        }

        val source: AndroidConnectionSource = get()

        source.connectionPool[given] = expected

        launch {
            source.openConnection(given)
        }

        unmockkStatic(SQLiteDatabase::class)

        assertTrue(source.connectionPool.isNotEmpty())
        assertNotNull(source.connectionPool)
        assertEquals(source.connectionPool.size, 1)
        assertTrue(source.connectionPool.containsKey(given))
        assertEquals(expected, source.connectionPool[given])
    }

    @Test
    fun `Close connection removes existing key from connection pool`() {
        val given = "test.db"

        val expected: SQLiteDatabase = mockk {
            every { isOpen } returns true
            every { close() } returns Unit
        }

        mockkStatic(SQLiteDatabase::class)
        every { SQLiteDatabase.openOrCreateDatabase(given, null) } returns mockk {
            every { path } returns given
            every { isOpen } returns true
            every { close() } returns Unit
        }

        val source: AndroidConnectionSource = get()

        source.connectionPool[given] = expected

        launch {
            source.closeConnection(given)
        }

        unmockkStatic(SQLiteDatabase::class)

        assertTrue(source.connectionPool.isEmpty())
        assertNotNull(source.connectionPool)
        assertFalse(source.connectionPool.containsKey(given))
    }

    @Test
    fun `Close connection does nothing for non-existing key in connection pool`() {
        val given = "test.db"

        mockkStatic(SQLiteDatabase::class)
        every { SQLiteDatabase.openOrCreateDatabase(given, null) } returns mockk {
            every { path } returns given
            every { isOpen } returns true
            every { close() } returns Unit
        }

        val source: AndroidConnectionSource = get()

        launch {
            source.closeConnection(given)
        }

        unmockkStatic(SQLiteDatabase::class)

        assertTrue(source.connectionPool.isEmpty())
        assertNotNull(source.connectionPool)
        assertFalse(source.connectionPool.containsKey(given))
    }
}
