package com.infinum.dbinspector.ui.shared.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class EditorTextInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TextInputEditText(context, attrs, defStyle) {

    val rect = Rect()
    val paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
        textSize = 80.0f
    }

    override fun onDraw(canvas: Canvas) {
        for (i in 0 until lineCount) {
            canvas.drawText("" + (i + 1), rect.left.toFloat(), baseline.toFloat(), paint)
        }
        super.onDraw(canvas)
    }
}
