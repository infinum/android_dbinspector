package com.infinum.dbinspector.domain.database.control

import com.infinum.dbinspector.domain.Control
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
            single { mockk<Mappers.Database>() }
            single { mockk<Converters.Database>() }
            factory<Control.Database> { DatabaseControl(get(), get()) }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val control: Control.Database = get()

        assertNotNull(control)
    }

    @Test
    fun `Control converter is not null`() {
        val control: Control.Database = get()

        assertNotNull(control.converter)
    }

    @Test
    fun `Control mapper is not null`() {
        val control: Control.Database = get()

        assertNotNull(control.mapper)
    }
}
