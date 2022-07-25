package com.infinum.dbinspector.data.models.local.cursor.output

public enum class FieldType {
    NULL,
    INTEGER,
    FLOAT,
    STRING,
    BLOB;

    public companion object {

        public operator fun invoke(value: Int): FieldType = values().single { it.ordinal == value }
    }
}
