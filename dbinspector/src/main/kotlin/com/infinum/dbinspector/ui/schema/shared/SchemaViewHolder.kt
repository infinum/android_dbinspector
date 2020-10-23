package com.infinum.dbinspector.ui.schema.shared

import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.databinding.DbinspectorItemSchemaBinding

internal class SchemaViewHolder(
    private val viewBinding: DbinspectorItemSchemaBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(item: String?, onClick: (String) -> Unit) {
        item?.let { table ->
            with(viewBinding) {
                this.name.text = table
                this.root.setOnClickListener { onClick(table) }
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
