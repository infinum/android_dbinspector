package com.infinum.dbinspector.ui.shared.views.editor

import androidx.annotation.DrawableRes
import com.infinum.dbinspector.R

enum class TokenType(@DrawableRes val backgroundResId: Int) {
    SQL(R.drawable.dbinspector_keyword_sql_background),
    NAME(R.drawable.dbinspector_keyword_name_background)
}