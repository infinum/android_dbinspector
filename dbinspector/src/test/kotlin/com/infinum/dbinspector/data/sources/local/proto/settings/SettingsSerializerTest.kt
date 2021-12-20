package com.infinum.dbinspector.data.sources.local.proto.settings

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.shared.BaseTest
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import java.io.InputStream
import java.io.OutputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.module.Module
import org.mockito.kotlin.any

@DisplayName("SettingsSerializer tests")
internal class SettingsSerializerTest : BaseTest() {

    override fun modules(): List<Module> = listOf()

    @Test
    fun `Serializer has default value`() {
        val expected = SettingsEntity.getDefaultInstance()
        val serializer = SettingsSerializer()

        assertNotNull(serializer.defaultValue)
        assertEquals(expected, serializer.defaultValue)
    }

    @Test
    @Disabled("Mockk says: 'The InputStream implementation is buggy.'")
    fun `Serializer reads stream into entity`() {
        val serializer = SettingsSerializer()

        mockkStatic(SettingsEntity::class)
        every { SettingsEntity.parseFrom(mockk<InputStream>()) } returns mockk()

        launch {
            serializer.readFrom(any())
        }

        unmockkStatic(SettingsEntity::class)

        coVerify(exactly = 1) { SettingsEntity.parseFrom(mockk<InputStream>()) }
    }

    @Test
    @Disabled("No idea how to test streams")
    fun `Serializer writes entity to stream`() {
        val given = SettingsEntity.getDefaultInstance()
        val serializer = SettingsSerializer()
        val stream: OutputStream = mockk()

        launch {
            serializer.writeTo(given, stream)
        }

        verify(exactly = 1) { given.writeTo(stream) }
    }
}
