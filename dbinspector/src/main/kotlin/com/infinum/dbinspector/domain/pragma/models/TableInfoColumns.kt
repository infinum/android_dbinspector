package com.infinum.dbinspector.domain.pragma.models

internal enum class TableInfoColumns {
    CID,
    NAME,
    TYPE,
    NOTNULL,
    DFLT,
    PK;

    companion object {

        operator fun invoke(index: Int) = values().single { it.ordinal == index }
    }
}
