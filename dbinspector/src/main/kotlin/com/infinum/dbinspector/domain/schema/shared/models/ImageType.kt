package com.infinum.dbinspector.domain.schema.shared.models

internal enum class ImageType(val header: String) {
    UNSUPPORTED(""),
    JPG("FFD8FFE0"),
    TIFF("49492A"),
    GIF("474946"),
    BMP("424D"),
    PNG("89504E470D0A1A0A");

    companion object {

        operator fun invoke(hexData: String) = values().find {
            hexData.startsWith(it.header)
        } ?: UNSUPPORTED
    }
}