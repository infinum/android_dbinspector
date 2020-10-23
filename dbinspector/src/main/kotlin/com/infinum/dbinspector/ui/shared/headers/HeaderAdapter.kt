package com.infinum.dbinspector.ui.shared.headers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinum.dbinspector.databinding.DbinspectorItemHeaderBinding

internal class HeaderAdapter(
    private var headerItems: List<Header>,
    private val isClickable: Boolean = false,
    private val onClick: ((Header) -> Unit)? = null
) : RecyclerView.Adapter<HeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder =
        HeaderViewHolder(
            DbinspectorItemHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) =
        holder.bind(
            headerItems[position % headerItems.size],
            isClickable,
            onClick
        )

    override fun onViewRecycled(holder: HeaderViewHolder) =
        with(holder) {
            holder.unbind()
            super.onViewRecycled(this)
        }

    override fun getItemCount(): Int =
        headerItems.size

    fun updateHeader(header: Header) {
        headerItems = headerItems.map {
            if (it.name == header.name) {
                header.copy(active = true)
            } else {
                it.copy(active = false)
            }
        }
        notifyDataSetChanged()
    }
}
