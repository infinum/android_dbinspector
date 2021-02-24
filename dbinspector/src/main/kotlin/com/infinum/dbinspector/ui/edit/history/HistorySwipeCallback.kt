package com.infinum.dbinspector.ui.edit.history

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.R
import kotlin.math.roundToInt

internal class HistorySwipeCallback(
    context: Context,
    private val onSwipe: (position: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    private val icon = ContextCompat.getDrawable(context, R.drawable.dbinspector_ic_clear_item)
    private val background = ColorDrawable(ContextCompat.getColor(context, R.color.dbinspector_color_remove))
    private val backgroundCornerOffset = 0

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        onSwipe(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView: View = viewHolder.itemView

        val iconMargin = (itemView.height - (icon?.intrinsicHeight ?: 0)) / 2.0f
        val iconTop = itemView.top + (itemView.height - (icon?.intrinsicHeight ?: 0)) / 2.0f
        val iconBottom = iconTop + (icon?.intrinsicHeight ?: 0)

        when {
            // Swiping to the right
            dX > 0 -> {
                val iconLeft = (itemView.left + iconMargin + (icon?.intrinsicWidth ?: 0)).roundToInt()
                val iconRight = (itemView.left + iconMargin).roundToInt()
                icon?.setBounds(iconLeft, iconTop.roundToInt(), iconRight, iconBottom.roundToInt())

                background.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.left + dX.roundToInt() + backgroundCornerOffset,
                    itemView.bottom
                )
            }
            // Swiping to the left
            dX < 0 -> {
                val iconLeft = (itemView.right - iconMargin - (icon?.intrinsicWidth ?: 0)).roundToInt()
                val iconRight = (itemView.right - iconMargin).roundToInt()
                icon?.setBounds(iconLeft, iconTop.roundToInt(), iconRight, iconBottom.roundToInt())

                background.setBounds(
                    itemView.right + dX.roundToInt() - backgroundCornerOffset,
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
            // view is unSwiped
            else -> {
                icon?.setBounds(
                    0,
                    0,
                    0,
                    0
                )
                background.setBounds(
                    0,
                    0,
                    0,
                    0
                )
            }
        }
        background.draw(c)
        icon?.draw(c)
    }
}
