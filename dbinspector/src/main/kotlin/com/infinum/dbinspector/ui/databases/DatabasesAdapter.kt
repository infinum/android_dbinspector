package com.infinum.dbinspector.ui.databases

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.infinum.dbinspector.databinding.DbinspectorItemDatabaseBinding
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor

internal class DatabasesAdapter(
    private val onClick: (DatabaseDescriptor) -> Unit,
    private val interactions: DatabaseInteractions,
    private val onEmpty: (Boolean) -> Unit
) : ListAdapter<DatabaseDescriptor, DatabaseViewHolder>(DatabaseDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatabaseViewHolder =
        DatabaseViewHolder(
            DbinspectorItemDatabaseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DatabaseViewHolder, position: Int) =
        holder.bind(
            item = getItem(position),
            onClick = onClick,
            interactions = interactions
        )

    override fun onViewRecycled(holder: DatabaseViewHolder) =
        with(holder) {
            unbind()
            super.onViewRecycled(this)
        }

    override fun onCurrentListChanged(
        previousList: MutableList<DatabaseDescriptor>,
        currentList: MutableList<DatabaseDescriptor>
    ) {
        onEmpty(currentList.isEmpty())
    }
}
