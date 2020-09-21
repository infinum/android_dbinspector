package im.dino.dbinspector.domain.database.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Database(
    val absolutePath: String,
    val path: String,
    val name: String,
    val version: String
) : Parcelable