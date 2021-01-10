package com.infinum.dbinspector.ui.shared.views.editor

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.infinum.dbinspector.R

internal enum class KeywordType(
    @DrawableRes val backgroundResId: Int,
    @ColorRes val textColorResId: Int
) {
    SQLITE_KEYWORD(
        backgroundResId = R.drawable.dbinspector_keyword_sqlite_keyword_background,
        textColorResId = android.R.color.white
    ),
    SQLITE_FUNCTION(
        backgroundResId = R.drawable.dbinspector_keyword_sqlite_function_background,
        textColorResId = R.color.dbinspector_color_keyword_sql_function
    ),
    SQLITE_TYPE(
        backgroundResId = R.drawable.dbinspector_keyword_sqlite_type_background,
        textColorResId = R.color.dbinspector_color_keyword_sql_type
    ),
    TABLE_NAME(
        backgroundResId = R.drawable.dbinspector_keyword_table_name_background,
        textColorResId = R.color.dbinspector_color_keyword_table_name
    ),
    VIEW_NAME(
        backgroundResId = R.drawable.dbinspector_keyword_table_name_background,
        textColorResId = R.color.dbinspector_color_keyword_view_name
    ),
    TRIGGER_NAME(
        backgroundResId = R.drawable.dbinspector_keyword_table_name_background,
        textColorResId = R.color.dbinspector_color_keyword_trigger_name
    ),
    COLUMN_NAME(
        backgroundResId = R.drawable.dbinspector_keyword_column_name_background,
        textColorResId = R.color.dbinspector_color_keyword_column_name
    )
}
