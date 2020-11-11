package com.infinum.dbinspector.domain.shared.models

import androidx.annotation.DrawableRes
import com.infinum.dbinspector.R

enum class Direction(val rawValue: String, @DrawableRes val icon: Int) {
    ASCENDING("ASC", R.drawable.dbinspector_ic_sort_arrows_desc),
    DESCENDING("DESC", R.drawable.dbinspector_ic_sort_arrows_asc)
}
