package com.infinum.dbinspector.domain.settings.control

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

@DisplayName("SettingsControl tests")
internal class SettingsControlTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Mappers.Settings>() }
            factory { mockk<Converters.Settings>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val control = SettingsControl(get(), get())

        assertNotNull(control)
    }

    @Test
    fun `Control converter is not null`() {
        val converter: Converters.Settings = get()
        val control = SettingsControl(get(), converter)

        assertNotNull(control.converter)
    }

    @Test
    fun `Control mapper is not null`() {
        val mapper: Mappers.Settings = get()
        val control = SettingsControl(mapper, get())

        assertNotNull(control.mapper)
    }
}
