package im.dino.dbinspector.ui.content.shared

import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.databinding.DbinspectorItemHeaderBinding

internal class HeaderViewHolder(
    private val viewBinding: DbinspectorItemHeaderBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: String) {
        with(viewBinding) {
            this.valueView.text = item
        }
    }
}
