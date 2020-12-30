package com.infinum.dbinspector.data.source.local.proto.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import java.io.InputStream
import java.io.OutputStream

internal class SettingsSerializer : Serializer<SettingsEntity> {

    override val defaultValue: SettingsEntity =
        SettingsEntity.getDefaultInstance()

    override fun readFrom(input: InputStream): SettingsEntity {
        try {
            return SettingsEntity.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: SettingsEntity, output: OutputStream) =
        t.writeTo(output)
}
