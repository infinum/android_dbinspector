package com.infinum.dbinspector.domain.pragma.models

internal enum class IndexListColumns {
    SEQ,
    NAME,
    UNIQUE;

    companion object {

        operator fun invoke(index: Int) = values().single { it.ordinal == index }
    }
}
