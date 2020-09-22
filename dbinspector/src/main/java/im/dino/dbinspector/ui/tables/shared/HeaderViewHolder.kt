package im.dino.dbinspector.ui.tables.shared

import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.databinding.DbinspectorItemHeaderBinding

class HeaderViewHolder(
    private val viewBinding: DbinspectorItemHeaderBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: String) {
        with(viewBinding) {
            this.valueView.text = item
//            this.content.setOnClickListener { onClick(it) }
        }

    }

    fun unbind() {
        with(viewBinding) {
//            this.content.setOnClickListener(null)
        }
    }
}