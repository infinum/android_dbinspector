package com.infinum.dbinspector.data.models.local.cursor

internal enum class BlobPreviewType {
    UNSUPPORTED,
    PLACEHOLDER,
    UTF_8,
    HEX,
    BASE_64;

    companion object {

        operator fun invoke(value: Int) = values().single { it.ordinal == value }
    }
}
