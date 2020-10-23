package com.infinum.dbinspector.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat

internal val Context.databaseDir: String
    get() = "${filesDir.path.substring(0, filesDir.path.lastIndexOf("/"))}/databases"

internal fun Context.drawableFromAttribute(attrId: Int): Drawable {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(attrId, typedValue, true)
    val imageResId = typedValue.resourceId
    return ContextCompat.getDrawable(this, imageResId)
        ?: throw IllegalArgumentException("Cannot load drawable $imageResId")
}
