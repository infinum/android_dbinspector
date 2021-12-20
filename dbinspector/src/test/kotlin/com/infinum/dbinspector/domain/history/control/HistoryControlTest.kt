package com.infinum.dbinspector.domain.history.control

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
            factory { mockk<Mappers.History>() }
            factory { mockk<Converters.History>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val control = HistoryControl(get(), get())

        assertNotNull(control)
    }

    @Test
    fun `Control converter is not null`() {
        val converter: Converters.History = get()
        val control = HistoryControl(get(), converter)

        assertNotNull(control.converter)
    }

    @Test
    fun `Control mapper is not null`() {
        val mapper: Mappers.History = get()
        val control = HistoryControl(mapper, get())

        assertNotNull(control.mapper)
    }
}
