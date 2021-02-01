package com.infinum.dbinspector.extensions

import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R

internal fun RecyclerView.setupAsTable() {
    ContextCompat.getDrawable(
        context,
        R.drawable.dbinspector_divider_vertical
    )?.let { drawable ->
        val verticalDecorator = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        verticalDecorator.setDrawable(drawable)
        addItemDecoration(verticalDecorator)
    }
    ContextCompat.getDrawable(
        context,
        R.drawable.dbinspector_divider_horizontal
    )?.let { drawable ->
        val horizontalDecorator = DividerItemDecoration(
            context,
            DividerItemDecoration.HORIZONTAL
        )
        horizontalDecorator.setDrawable(drawable)
        addItemDecoration(horizontalDecorator)
    }
    updateLayoutParams {
        minimumWidth = resources.displayMetrics.widthPixels
    }
}
