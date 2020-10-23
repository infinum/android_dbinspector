package com.infinum.dbinspector.ui.shared.diffutils

import androidx.recyclerview.widget.DiffUtil

internal class FieldDiffUtil : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
