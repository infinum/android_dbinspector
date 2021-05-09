package com.infinum.dbinspector.domain.history.control

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

@DisplayName("HistoryControl tests")
internal class HistoryControlTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            single { mockk<Mappers.History>() }
            single { mockk<Converters.History>() }
            factory<Control.History> { HistoryControl(get(), get()) }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val control: Control.History = get()

        assertNotNull(control)
    }

    @Test
    fun `Control converter is not null`() {
        val control: Control.History = get()

        assertNotNull(control.converter)
    }

    @Test
    fun `Control mapper is not null`() {
        val control: Control.History = get()

        assertNotNull(control.mapper)
    }
}
