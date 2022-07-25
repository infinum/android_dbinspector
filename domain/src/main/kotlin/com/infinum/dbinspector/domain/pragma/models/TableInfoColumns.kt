package com.infinum.dbinspector.domain.pragma.models

public enum class TableInfoColumns {
    CID,
    NAME,
    TYPE,
    NOTNULL,
    DFLT,
    PK;

    public companion object {

        public operator fun invoke(index: Int): TableInfoColumns = values().single { it.ordinal == index }
    }
}
