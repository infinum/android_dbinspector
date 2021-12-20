package com.infinum.dbinspector.domain.database.control.mappers

import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.every
import io.mockk.mockk
import java.io.File
import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module

@DisplayName("DatabaseMapper tests")
internal class DatabaseMapperTest : BaseTest() {

    override fun modules(): List<Module> = listOf()

    @Test
    fun `Database name with extension maps to domain with same values for name and extension`() =
        launch {
            val given = mockk<File> {
                every { path } returns "test.db"
                every { exists() } returns true
                every { parentFile } returns mockk {
                    every { absolutePath } returns ""
                }
                every { absolutePath } returns "/test.db"
                every { name } returns "test.db"
            }
            val expected = DatabaseDescriptor(
                exists = true,
                name = "test",
                extension = "db",
                parentPath = ""
            )

            val mapper = DatabaseMapper()
            val actual = test {
                mapper(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Database name without extension maps to domain with same values for name only`() =
        launch {
            val given = mockk<File> {
                every { path } returns "test"
                every { exists() } returns true
                every { parentFile } returns mockk {
                    every { absolutePath } returns ""
                }
                every { absolutePath } returns "/test"
                every { name } returns "test"
            }
            val expected = DatabaseDescriptor(
                exists = true,
                name = "test",
                extension = "",
                parentPath = ""
            )
            val mapper = DatabaseMapper()
            val actual = test {
                mapper(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Database without name maps to domain with no values for name`() =
        launch {
            val given = mockk<File> {
                every { path } returns ""
                every { exists() } returns true
                every { parentFile } returns mockk {
                    every { absolutePath } returns ""
                }
                every { absolutePath } returns "/.db"
                every { name } returns ".db"
            }
            val expected = DatabaseDescriptor(
                exists = true,
                name = "",
                extension = "db",
                parentPath = ""
            )

            val mapper = DatabaseMapper()
            val actual = test {
                mapper(given)
            }

            assertEquals(expected, actual)
        }

    @Test
    fun `Database without name and extension maps to domain with no values for name and extension`() =
        launch {
            val given = mockk<File> {
                every { path } returns ""
                every { exists() } returns false
                every { parentFile } returns mockk {
                    every { absolutePath } returns ""
                }
                every { absolutePath } returns "/"
                every { name } returns ""
            }
            val expected = DatabaseDescriptor(
                exists = false,
                name = "",
                extension = "",
                parentPath = ""
            )

            val mapper = DatabaseMapper()
            val actual = test {
                mapper(given)
            }

            assertEquals(expected, actual)
        }
}
