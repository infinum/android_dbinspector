package com.infinum.dbinspector.domain.shared.control

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

@DisplayName("ContentControl tests")
internal class ContentControlTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<Mappers.Content>() }
            factory { mockk<Converters.Content>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val control = ContentControl(get(), get())

        assertNotNull(control)
    }

    @Test
    fun `Control converter is not null`() {
        val converter: Converters.Content = get()
        val control = ContentControl(get(), converter)

        assertNotNull(control.converter)
    }

    @Test
    fun `Control mapper is not null`() {
        val mappers: Mappers.Content = get()
        val control = ContentControl(mappers, get())

        assertNotNull(control.mapper)
    }
}
