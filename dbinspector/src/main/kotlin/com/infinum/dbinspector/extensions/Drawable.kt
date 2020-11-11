package com.infinum.dbinspector.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes

internal fun Drawable.scale(context: Context, @DimenRes widthResId: Int, @DimenRes heightResId: Int): Drawable {
    val bitmap = Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
    Canvas(bitmap).apply {
        this@scale.setBounds(0, 0, width, height)
        this@scale.draw(this)
    }
    return BitmapDrawable(
        Resources.getSystem(),
        Bitmap.createScaledBitmap(
            bitmap,
            context.resources.getDimensionPixelSize(widthResId),
            context.resources.getDimensionPixelSize(heightResId),
            true
        )
    )
}
