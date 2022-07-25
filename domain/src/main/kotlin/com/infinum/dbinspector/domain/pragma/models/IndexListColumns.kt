package com.infinum.dbinspector.domain.pragma.models

public enum class IndexListColumns {
    SEQ,
    NAME,
    UNIQUE;

    public companion object {

        public operator fun invoke(index: Int): IndexListColumns = values().single { it.ordinal == index }
    }
}
