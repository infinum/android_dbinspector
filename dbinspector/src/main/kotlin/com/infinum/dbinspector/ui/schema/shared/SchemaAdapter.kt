package com.infinum.dbinspector.ui.schema.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.infinum.dbinspector.databinding.DbinspectorItemSchemaBinding
import com.infinum.dbinspector.ui.shared.diffutils.FieldDiffUtil

internal class SchemaAdapter(
    private val onClick: (String) -> Unit
) : PagingDataAdapter<String, SchemaViewHolder>(FieldDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchemaViewHolder =
        SchemaViewHolder(DbinspectorItemSchemaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SchemaViewHolder, position: Int) =
        holder.bind(getItem(position), onClick)

    override fun onViewRecycled(holder: SchemaViewHolder) =
        with(holder) {
            unbind()
            super.onViewRecycled(this)
        }
}
