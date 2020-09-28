package im.dino.dbinspector.ui.pragma.schema

import androidx.recyclerview.widget.DiffUtil

internal class PragmaDiffUtil : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}