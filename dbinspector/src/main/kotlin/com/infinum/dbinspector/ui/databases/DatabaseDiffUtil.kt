package com.infinum.dbinspector.ui.databases

import androidx.recyclerview.widget.DiffUtil
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor

internal class DatabaseDiffUtil : DiffUtil.ItemCallback<DatabaseDescriptor>() {

    override fun areItemsTheSame(oldItem: DatabaseDescriptor, newItem: DatabaseDescriptor): Boolean {
        return oldItem.absolutePath == newItem.absolutePath
    }

    override fun areContentsTheSame(oldItem: DatabaseDescriptor, newItem: DatabaseDescriptor): Boolean {
        return oldItem == newItem
    }
}
