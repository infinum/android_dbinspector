package com.infinum.dbinspector.domain.pragma.models

internal enum class TriggerInfoColumns {
    NAME,
    SQL;

    companion object {

        operator fun invoke(index: Int) = values().single { it.ordinal == index }
    }
}
