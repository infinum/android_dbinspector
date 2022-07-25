package com.infinum.dbinspector.domain.pragma.models

public enum class ForeignKeyListColumns {
    ID,
    SEQ,
    TABLE,
    FROM,
    TO,
    ON_UPDATE,
    ON_DELETE,
    MATCH;

    public companion object {

        public operator fun invoke(index: Int): ForeignKeyListColumns = values().single { it.ordinal == index }
    }
}
