package com.infinum.dbinspector.domain.shared.models

public enum class BlobPreviewMode {
    UNSUPPORTED,
    PLACEHOLDER,
    UTF_8,
    HEX,
    BASE_64;

    public companion object {

        public operator fun invoke(value: Int): BlobPreviewMode = values().single { it.ordinal == value }
    }
}
