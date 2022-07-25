package com.infinum.dbinspector.domain.schema.shared.models

public enum class ImageType(public val header: String, public val suffix: String) {
    UNSUPPORTED("", ""),
    JPG("FFD8FFE0", ".jpg"),
    PNG("89504E470D0A1A0A", ".png"),
    BMP("424D", ".bmp");

    public companion object {

        public operator fun invoke(hexData: String): ImageType =
            when {
                hexData.startsWith(prefix = JPG.header, ignoreCase = true) -> JPG
                hexData.startsWith(prefix = PNG.header, ignoreCase = true) -> PNG
                hexData.startsWith(prefix = BMP.header, ignoreCase = true) -> BMP
                else -> UNSUPPORTED
            }
    }
}
