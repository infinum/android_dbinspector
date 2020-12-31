package com.infinum.dbinspector.ui.schema.shared

import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.databinding.DbinspectorItemSchemaBinding
import com.infinum.dbinspector.domain.shared.models.Cell

internal class SchemaViewHolder(
    private val viewBinding: DbinspectorItemSchemaBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: Cell?, onClick: (String) -> Unit) {
        item?.let { table ->
            with(viewBinding) {
                this.name.text = table.text
                this.root.setOnClickListener { table.text?.let { onClick(it) } }
            }
        } ?: with(viewBinding) {
            this.name.text = null
            this.root.setOnClickListener(null)
        }
    }

    fun unbind() {
        with(viewBinding) {
            this.root.setOnClickListener(null)
        }
    }
}
