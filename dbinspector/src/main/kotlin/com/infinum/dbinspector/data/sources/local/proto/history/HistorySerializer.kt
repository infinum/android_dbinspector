package com.infinum.dbinspector.data.sources.local.proto.history

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import java.io.InputStream
import java.io.OutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

internal class HistorySerializer : Serializer<HistoryEntity> {

    override val defaultValue: HistoryEntity =
        HistoryEntity.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): HistoryEntity =
        suspendCancellableCoroutine { continuation ->
            try {
                continuation.resume(HistoryEntity.parseFrom(input))
            } catch (exception: InvalidProtocolBufferException) {
                continuation.resumeWithException(CorruptionException("Cannot read proto.", exception))
            }
        }

    override suspend fun writeTo(t: HistoryEntity, output: OutputStream): Unit =
        suspendCancellableCoroutine { continuation ->
            t.writeTo(output)
            continuation.resume(Unit)
        }
}
