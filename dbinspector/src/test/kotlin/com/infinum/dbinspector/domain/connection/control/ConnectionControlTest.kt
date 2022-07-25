package com.infinum.dbinspector.domain.connection.control

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

@DisplayName("ConnectionControl tests")
internal class ConnectionControlTest : BaseTest() {

    override fun modules(): List<Module> = listOf(
        module {
            factory { mockk<com.infinum.dbinspector.domain.Mappers.Connection>() }
            factory { mockk<com.infinum.dbinspector.domain.Converters.Connection>() }
        }
    )

    @Test
    fun `Can be instantiated`() {
        val control = ConnectionControl(get(), get())

        assertNotNull(control)
    }

    @Test
    fun `Control converter is not null`() {
        val converter: com.infinum.dbinspector.domain.Converters.Connection = get()
        val control = ConnectionControl(get(), converter)

        assertNotNull(control.converter)
    }

    @Test
    fun `Control mapper is not null`() {
        val mapper: com.infinum.dbinspector.domain.Mappers.Connection = get()
        val control = ConnectionControl(mapper, get())

        assertNotNull(control.mapper)
    }
}
