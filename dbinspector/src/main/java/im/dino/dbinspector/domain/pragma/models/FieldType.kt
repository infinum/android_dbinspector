package im.dino.dbinspector.domain.pragma.models

enum class FieldType {
    NULL,
    INTEGER,
    FLOAT,
    STRING,
    BLOB;

    companion object {

        operator fun invoke(value: Int) = values().firstOrNull { it.ordinal == value }
    }
}