package com.infinum.dbinspector.data.models.local

internal enum class FieldType {
    NULL,
    INTEGER,
    FLOAT,
    STRING,
    BLOB;

    companion object {

        operator fun invoke(value: Int) = values().single { it.ordinal == value }
    }
}
