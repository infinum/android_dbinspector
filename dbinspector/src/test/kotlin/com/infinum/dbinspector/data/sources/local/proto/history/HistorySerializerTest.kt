package com.infinum.dbinspector.data.sources.local.proto.history

import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
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

@DisplayName("HistorySerializer tests")
internal class HistorySerializerTest : BaseTest() {

    override fun modules(): List<Module> = listOf()

    @Test
    fun `Serializer has default value`() {
        val expected = HistoryEntity.getDefaultInstance()
        val serializer = HistorySerializer()

        assertNotNull(serializer.defaultValue)
        assertEquals(expected, serializer.defaultValue)
    }

    @Test
    @Disabled("Mockk says: 'The InputStream implementation is buggy.'")
    fun `Serializer reads stream into entity`() {
        val serializer = HistorySerializer()

        mockkStatic(HistoryEntity::class)
        every { HistoryEntity.parseFrom(mockk<InputStream>()) } returns mockk()

        test {
            serializer.readFrom(any())
        }

        unmockkStatic(HistoryEntity::class)

        coVerify(exactly = 1) { HistoryEntity.parseFrom(mockk<InputStream>()) }
    }

    @Test
    @Disabled("No idea how to test streams")
    fun `Serializer writes entity to stream`() {
        val given = HistoryEntity.getDefaultInstance()
        val serializer = HistorySerializer()
        val stream: OutputStream = mockk()

        test {
            serializer.writeTo(given, stream)
        }

        verify(exactly = 1) { given.writeTo(stream) }
    }
}
