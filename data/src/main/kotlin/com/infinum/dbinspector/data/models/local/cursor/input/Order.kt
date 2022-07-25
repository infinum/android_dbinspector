package com.infinum.dbinspector.data.models.local.cursor.input

import androidx.annotation.DrawableRes
import com.infinum.dbinspector.R

public enum class Order(public val rawValue: String, @DrawableRes public val icon: Int) {
    ASCENDING("ASC", R.drawable.dbinspector_ic_sort_arrows_desc),
    DESCENDING("DESC", R.drawable.dbinspector_ic_sort_arrows_asc);

    public companion object {

        public operator fun invoke(value: String): Order = values().firstOrNull { it.rawValue == value }
            ?: ASCENDING
    }
}
