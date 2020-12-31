package com.infinum.dbinspector.domain.schema.shared.models

internal enum class ImageType(val header: String, val suffix: String) {
    UNSUPPORTED("", ""),
    JPG("FFD8FFE0", ".jpg"),
    PNG("89504E470D0A1A0A", ".png"),
    BMP("424D", ".bmp");

    companion object {

        operator fun invoke(hexData: String): ImageType =
            when {
                hexData.startsWith(prefix = JPG.header, ignoreCase = true) -> JPG
                hexData.startsWith(prefix = PNG.header, ignoreCase = true) -> PNG
                hexData.startsWith(prefix = BMP.header, ignoreCase = true) -> BMP
                else -> UNSUPPORTED
            }
    }
}
