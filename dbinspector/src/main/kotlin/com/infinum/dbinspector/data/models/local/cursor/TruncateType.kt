package com.infinum.dbinspector.data.models.local.cursor

internal enum class TruncateType {
    START,
    MIDDLE,
    END;

    companion object {

        operator fun invoke(value: Int) = values().single { it.ordinal == value }
    }
}
