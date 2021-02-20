package com.infinum.dbinspector.data.source.local.proto.history

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import java.io.InputStream
import java.io.OutputStream

internal class HistorySerializer : Serializer<HistoryEntity> {

    override val defaultValue: HistoryEntity =
        HistoryEntity.getDefaultInstance()

    override fun readFrom(input: InputStream): HistoryEntity {
        try {
            return HistoryEntity.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: HistoryEntity, output: OutputStream) =
        t.writeTo(output)
}
