package com.infinum.dbinspector.domain.schema.shared.models

import androidx.annotation.StringRes
import com.infinum.dbinspector.R

public enum class SchemaType(@StringRes public val nameRes: Int) {
    TABLE(R.string.dbinspector_schema_tables),
    VIEW(R.string.dbinspector_schema_views),
    TRIGGER(R.string.dbinspector_schema_triggers);

    public companion object {

        public operator fun invoke(index: Int): SchemaType = values().single { it.ordinal == index }
    }
}
