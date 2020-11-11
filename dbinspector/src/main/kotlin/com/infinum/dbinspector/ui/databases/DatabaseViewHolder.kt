package com.infinum.dbinspector.ui.databases

import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.databinding.DbinspectorItemDatabaseBinding
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor

internal class DatabaseViewHolder(
    private val viewBinding: DbinspectorItemDatabaseBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(
        item: DatabaseDescriptor,
        onClick: (DatabaseDescriptor) -> Unit,
        interactions: DatabaseInteractions
    ) {
        with(viewBinding) {
            this.path.text = item.parentPath
            this.name.text = item.name
            this.version.text = item.version
            this.removeButton.setOnClickListener { interactions.onDelete(item) }
            this.editButton.setOnClickListener { interactions.onEdit(item) }
            this.copyButton.setOnClickListener { interactions.onCopy(item) }
            this.shareButton.setOnClickListener { interactions.onShare(item) }
            this.content.setOnClickListener { onClick(item) }
        }
    }

    fun unbind() {
        with(viewBinding) {
            this.removeButton.setOnClickListener(null)
            this.editButton.setOnClickListener(null)
            this.copyButton.setOnClickListener(null)
            this.shareButton.setOnClickListener(null)
            this.content.setOnClickListener(null)
        }
    }
}
