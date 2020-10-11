package im.dino.dbinspector.domain.schema.shared.models

import androidx.annotation.StringRes
import im.dino.dbinspector.R

internal enum class SchemaType(@StringRes val nameRes: Int) {
    TABLE(R.string.dbinspector_schema_tables),
    VIEW(R.string.dbinspector_schema_views),
    TRIGGER(R.string.dbinspector_schema_triggers)
}
