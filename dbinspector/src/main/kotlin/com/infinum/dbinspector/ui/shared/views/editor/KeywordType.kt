package com.infinum.dbinspector.ui.shared.views.editor

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.infinum.dbinspector.R

enum class KeywordType(
    @DrawableRes val backgroundResId: Int,
    @ColorRes val textColorResId: Int
) {
    SQL(
        backgroundResId = R.drawable.dbinspector_keyword_sql_background,
        textColorResId = android.R.color.white
    ),
    NAME(
        backgroundResId = R.drawable.dbinspector_keyword_name_background,
        textColorResId = R.color.dbinspector_color_keyword_name
    )
}
