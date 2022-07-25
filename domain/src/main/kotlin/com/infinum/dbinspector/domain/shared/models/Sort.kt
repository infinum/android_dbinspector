package com.infinum.dbinspector.domain.shared.models

import androidx.annotation.DrawableRes
import com.infinum.dbinspector.R

public enum class Sort(public val rawValue: String, @DrawableRes public val icon: Int) {
    ASCENDING("ASC", R.drawable.dbinspector_ic_sort_arrows_desc),
    DESCENDING("DESC", R.drawable.dbinspector_ic_sort_arrows_asc)
}
