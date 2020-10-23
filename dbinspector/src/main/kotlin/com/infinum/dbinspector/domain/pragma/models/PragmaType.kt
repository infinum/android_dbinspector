package com.infinum.dbinspector.domain.pragma.models

import androidx.annotation.StringRes
import com.infinum.dbinspector.R

internal enum class PragmaType(@StringRes val nameRes: Int) {
    TABLE_INFO(R.string.dbinspector_pragma_table_info),
    FOREIGN_KEY(R.string.dbinspector_pragma_foreign_key),
    INDEX(R.string.dbinspector_pragma_index)
}
