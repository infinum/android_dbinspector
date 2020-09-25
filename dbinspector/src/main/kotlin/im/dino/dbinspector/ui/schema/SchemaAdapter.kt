package im.dino.dbinspector.ui.schema

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import im.dino.dbinspector.databinding.DbinspectorItemSchemaBinding

internal class SchemaAdapter(
    private val onClick: (String) -> Unit
) : PagingDataAdapter<String, SchemaViewHolder>(SchemaDiffUtil()) {

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