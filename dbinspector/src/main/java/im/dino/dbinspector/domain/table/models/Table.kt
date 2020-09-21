package im.dino.dbinspector.domain.table.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Table(
    val name: String
) : Parcelable