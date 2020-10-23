package com.infinum.dbinspector.domain.schema.shared.models

import androidx.annotation.StringRes
import com.infinum.dbinspector.R

internal enum class SchemaType(@StringRes val nameRes: Int) {
    TABLE(R.string.dbinspector_schema_tables),
    VIEW(R.string.dbinspector_schema_views),
    TRIGGER(R.string.dbinspector_schema_triggers);

    companion object {

        operator fun invoke(index: Int) = values().single { it.ordinal == index }
    }
}
