package im.dino.dbinspector.ui.shared.headers

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.databinding.DbinspectorItemHeaderBinding
import im.dino.dbinspector.domain.shared.models.Direction

internal class HeaderViewHolder(
    private val viewBinding: DbinspectorItemHeaderBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(
        item: Header,
        isClickable: Boolean,
        onClick: ((Header) -> Unit)?
    ) {
        with(viewBinding) {
            val nextDirection = if (item.direction == Direction.ASCENDING) {
                Direction.DESCENDING
            } else {
                Direction.ASCENDING
            }

            this.valueView.text = item.name
            this.valueView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                ContextCompat.getDrawable(this.valueView.context, nextDirection.icon),
                null
            )
            this.valueView.isClickable = isClickable
            this.valueView.setOnClickListener {
                onClick?.invoke(
                    item.copy(direction = nextDirection)
                )
            }
        }
    }

    fun unbind() {
        with(viewBinding) {
            this.valueView.isClickable = false
            this.valueView.setOnClickListener(null)
        }
    }
}
