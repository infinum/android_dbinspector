package com.infinum.dbinspector.domain.pragma.control

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

@DisplayName("PragmaControl tests")
internal class PragmaControlTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Mappers.Pragma>() }
            single { mockk<Converters.Pragma>() }
            factory<Control.Pragma> { PragmaControl(get(), get()) }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val control: Control.Pragma = get()

        assertNotNull(control)
    }

    @Test
    fun `Control converter is not null`() {
        val control: Control.Pragma = get()

        assertNotNull(control.converter)
    }

    @Test
    fun `Control mapper is not null`() {
        val control: Control.Pragma = get()

        assertNotNull(control.mapper)
    }
}
