package im.dino.dbinspector.ui.tables.content

import androidx.recyclerview.widget.DiffUtil

class ContentDiffUtil : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}