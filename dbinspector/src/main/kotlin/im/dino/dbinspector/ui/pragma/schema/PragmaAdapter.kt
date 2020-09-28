package im.dino.dbinspector.ui.pragma.schema

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.databinding.DbinspectorItemCellBinding
import im.dino.dbinspector.databinding.DbinspectorItemHeaderBinding
import im.dino.dbinspector.ui.shared.content.HeaderViewHolder

internal class PragmaAdapter(
    private val headerItems: List<String>,
) : PagingDataAdapter<String, RecyclerView.ViewHolder>(PragmaDiffUtil()) {

    companion object {
        private const val HEADER = 0
        private const val ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            HEADER -> HeaderViewHolder(DbinspectorItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM -> PragmaViewHolder(DbinspectorItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw NotImplementedError()
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            HEADER -> (holder as? HeaderViewHolder)?.bind(headerItems[position % headerItems.size])
            ITEM -> {
                val itemPosition = position - headerItems.size
                val item = getItem(itemPosition)
                (holder as? PragmaViewHolder)?.bind(item, itemPosition / headerItems.size)
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) =
        with(holder) {
            when (holder) {
                is HeaderViewHolder -> (holder as? HeaderViewHolder)?.unbind()
                is PragmaViewHolder -> (holder as? PragmaViewHolder)?.unbind()
            }
            super.onViewRecycled(this)
        }

    override fun getItemViewType(position: Int): Int =
        if (position < headerItems.size) {
            HEADER
        } else {
            ITEM
        }

    override fun getItemCount(): Int {
        val items = super.getItemCount()
        return items + headerItems.size
    }
}