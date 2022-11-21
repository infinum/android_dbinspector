package com.infinum.dbinspector.data.sources.local.proto.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import java.io.InputStream
import java.io.OutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

internal class SettingsSerializer : Serializer<SettingsEntity> {

    companion object {
        private const val DEFAULT_LINES_COUNT = 100
        private const val DEFAULT_SERVER_PORT = "3700"
    }

    override val defaultValue: SettingsEntity =
        SettingsEntity.getDefaultInstance()
            .apply {
                this.toBuilder()
                    .setLinesLimit(false)
                    .setLinesCount(DEFAULT_LINES_COUNT)
                    .setTruncateMode(SettingsEntity.TruncateMode.END)
                    .setBlobPreview(SettingsEntity.BlobPreviewMode.PLACEHOLDER)
                    .addAllIgnoredTableNames(listOf())
                    .setServerRunning(false)
                    .setServerPort(DEFAULT_SERVER_PORT)
                    .build()
            }

    override suspend fun readFrom(input: InputStream): SettingsEntity =
        suspendCancellableCoroutine { continuation ->
            try {
                continuation.resume(SettingsEntity.parseFrom(input))
            } catch (exception: InvalidProtocolBufferException) {
                continuation.resumeWithException(CorruptionException("Cannot read proto.", exception))
            }
        }

    override suspend fun writeTo(t: SettingsEntity, output: OutputStream): Unit =
        suspendCancellableCoroutine { continuation ->
            t.writeTo(output)
            continuation.resume(Unit)
        }
}
