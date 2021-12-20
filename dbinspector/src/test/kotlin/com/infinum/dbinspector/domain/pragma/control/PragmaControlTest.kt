package com.infinum.dbinspector.domain.pragma.control

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
            factory { mockk<Mappers.Pragma>() }
            factory { mockk<Converters.Pragma>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val control = PragmaControl(get(), get())

        assertNotNull(control)
    }

    @Test
    fun `Control converter is not null`() {
        val converter: Converters.Pragma = get()
        val control = PragmaControl(get(), converter)

        assertNotNull(control.converter)
    }

    @Test
    fun `Control mapper is not null`() {
        val mapper: Mappers.Pragma = get()
        val control = PragmaControl(mapper, get())

        assertNotNull(control.mapper)
    }
}
