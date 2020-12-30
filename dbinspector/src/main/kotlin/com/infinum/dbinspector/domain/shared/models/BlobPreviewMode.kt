package com.infinum.dbinspector.domain.shared.models

import com.infinum.dbinspector.domain.shared.base.BaseParameters

// TODO: Should this inherit BaseParameters???
internal enum class BlobPreviewMode : BaseParameters {
    UNSUPPORTED,
    PLACEHOLDER,
    UTF_8,
    HEX,
    BASE_64;

    companion object {

        operator fun invoke(value: Int) = values().single { it.ordinal == value }
    }
}