package com.infinum.dbinspector.data.sources.memory.pagination

import com.infinum.dbinspector.shared.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("CursorPaginator tests")
internal class CursorPaginatorTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { CursorPaginator() }
        }
    )

    @Test
    fun `New instance has no page count`() {
        val expected = 0
        val paginator: CursorPaginator = get()

        assertEquals(expected, paginator.pageCount)
    }

    @Test
    fun `Less rows than page size sets one page count`() {
        val given = 1
        val pageSize = 10
        val expected = 1

        val paginator: CursorPaginator = get()
        paginator.setPageCount(given, pageSize)

        assertEquals(expected, paginator.pageCount)
    }

    @Test
    fun `No rows sets zero page count`() {
        val given = 0
        val pageSize = 10
        val expected = 0

        val paginator: CursorPaginator = get()
        paginator.setPageCount(given, pageSize)

        assertEquals(expected, paginator.pageCount)
    }

    @Test
    fun `More rows than page size sets greater than one page count`() {
        val given = 20
        val pageSize = 10
        val expected = 2

        val paginator: CursorPaginator = get()
        paginator.setPageCount(given, pageSize)

        assertEquals(expected, paginator.pageCount)
    }

    @Test
    fun `Zero page size passed is coerced to at least one and sets zero page count`() {
        val given = 0
        val pageSize = 0
        val expected = 0

        val paginator: CursorPaginator = get()
        paginator.setPageCount(given, pageSize)

        assertEquals(expected, paginator.pageCount)
    }

    @Test
    fun `Negative row count is coerced to at least zero and sets zero page count`() {
        val given = -1
        val pageSize = 1
        val expected = 0

        val paginator: CursorPaginator = get()
        paginator.setPageCount(given, pageSize)

        assertEquals(expected, paginator.pageCount)
    }

    @Test
    fun `Negative row count is coerced to zero and negative page size is coerced to one also sets zero page count`() {
        val given = -1
        val pageSize = -1
        val expected = 0

        val paginator: CursorPaginator = get()
        paginator.setPageCount(given, pageSize)

        assertEquals(expected, paginator.pageCount)
    }

    @Test
    fun `Null current page sets null next page`() {
        val given: Int? = null
        val expected: Int? = null

        val paginator: CursorPaginator = get()
        val actual = paginator.nextPage(given)

        assertEquals(expected, actual)
        assertNull(actual)
    }

    @Test
    fun `Zero page count sets null next page`() {
        val given = 1
        val expected: Int? = null

        val paginator: CursorPaginator = get()
        paginator.pageCount = 0
        val actual = paginator.nextPage(given)

        assertEquals(expected, actual)
        assertNull(actual)
    }

    @Test
    fun `Current page is last sets null next page`() {
        val given = 1
        val expected: Int? = null

        val paginator: CursorPaginator = get()
        paginator.pageCount = 1
        val actual = paginator.nextPage(given)

        assertEquals(expected, actual)
        assertNull(actual)
    }

    @Test
    fun `Current page is not last increments next page`() {
        val given = 1
        val expected = 2

        val paginator: CursorPaginator = get()
        paginator.pageCount = 3
        val actual = paginator.nextPage(given)

        assertEquals(expected, actual)
        assertNotNull(actual)
    }

    @Test
    fun `Negative current page is coerced to at least zero`() {
        val given = -1
        val expected = 1

        val paginator: CursorPaginator = get()
        paginator.pageCount = 2
        val actual = paginator.nextPage(given)

        assertEquals(expected, actual)
        assertNotNull(actual)
    }

    @Test
    fun `Row count smaller than page size returns correct boundary`() {
        val given = 1
        val pageSize = 10
        val rowCount = 5
        val expected = Paginator.Boundary(
            startRow = 0,
            endRow = 5
        )

        val paginator: CursorPaginator = get()
        val actual = paginator.boundary(given, pageSize, rowCount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Equal page size and row count returns correct boundary`() {
        val given = 1
        val pageSize = 10
        val rowCount = 10
        val expected = Paginator.Boundary(
            startRow = 0,
            endRow = 10
        )

        val paginator: CursorPaginator = get()
        val actual = paginator.boundary(given, pageSize, rowCount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Row count larger than page size returns correct boundary`() {
        val given = 1
        val pageSize = 10
        val rowCount = 15
        val expected = Paginator.Boundary(
            startRow = 0,
            endRow = 10
        )

        val paginator: CursorPaginator = get()
        val actual = paginator.boundary(given, pageSize, rowCount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Null page returns correct boundary`() {
        val given: Int? = null
        val pageSize = 10
        val rowCount = 15
        val expected = Paginator.Boundary(
            startRow = 0,
            endRow = 10
        )

        val paginator: CursorPaginator = get()
        val actual = paginator.boundary(given, pageSize, rowCount)

        assertEquals(expected, actual)
    }

    @Test
    fun `Negative row count and page size returns correct boundary`() {
        val given = -1
        val pageSize = -10
        val rowCount = 15
        val expected = Paginator.Boundary(
            startRow = 0,
            endRow = 1
        )

        val paginator: CursorPaginator = get()
        val actual = paginator.boundary(given, pageSize, rowCount)

        assertEquals(expected, actual)
    }
}
