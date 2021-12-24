package com.infinum.dbinspector.domain.database.control

import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.get

@DisplayName("DatabaseControl tests")
internal class DatabaseControlTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Mappers.Database>() }
            factory { mockk<Converters.Database>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val control = DatabaseControl(get(), get())

        assertNotNull(control)
    }

    @Test
    fun `Control converter is not null`() {
        val converter: Converters.Database = get()
        val control = DatabaseControl(get(), converter)

        assertNotNull(control.converter)
    }

    @Test
    fun `Control mapper is not null`() {
        val mapper: Mappers.Database = get()
        val control = DatabaseControl(mapper, get())

        assertNotNull(control.mapper)
    }
}
