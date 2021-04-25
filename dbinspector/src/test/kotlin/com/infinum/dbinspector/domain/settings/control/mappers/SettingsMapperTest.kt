package com.infinum.dbinspector.domain.settings.control.mappers

import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.mappers.BlobPreviewModeMapper
import com.infinum.dbinspector.domain.shared.mappers.TruncateModeMapper
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension

internal class SettingsMapperTest : KoinTest {

    private val truncateModeMapper by inject<Mappers.TruncateMode>()
    private val blobPreviewModeMapper by inject<Mappers.TruncateMode>()

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(
            module {
                single<Mappers.BlobPreviewMode> { BlobPreviewModeMapper() }
                single<Mappers.TruncateMode> { TruncateModeMapper() }
            }
        )
    }

    @Test
    fun `Given child mappers injected, when child mappers used, then child mappers are not null`() {
        assertNotNull(truncateModeMapper)
        assertNotNull(blobPreviewModeMapper)
    }

    @Test
    fun invoke() {
    }
}
