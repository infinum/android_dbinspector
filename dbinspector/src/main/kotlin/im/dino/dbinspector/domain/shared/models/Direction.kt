package im.dino.dbinspector.domain.shared.models

import androidx.annotation.DrawableRes
import im.dino.dbinspector.R

enum class Direction(val rawValue: String, @DrawableRes val icon: Int) {
    ASCENDING("ASC", R.drawable.dbinspector_ic_sort_arrows_desc),
    DESCENDING("DESC", R.drawable.dbinspector_ic_sort_arrows_asc)
}
