package com.infinum.dbinspector.domain.shared.models

internal enum class BlobPreviewMode {
    UNSUPPORTED,
    PLACEHOLDER,
    UTF_8,
    HEX,
    BASE_64;

    companion object {

        operator fun invoke(value: Int) = values().single { it.ordinal == value }
    }
}
