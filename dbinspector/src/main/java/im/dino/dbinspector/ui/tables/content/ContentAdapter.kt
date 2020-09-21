package im.dino.dbinspector.ui.tables.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import im.dino.dbinspector.databinding.DbinspectorItemCellBinding

class ContentAdapter(
    private val columnCount: Int
) : PagingDataAdapter<String, ContentViewHolder>(ContentDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder =
        ContentViewHolder(DbinspectorItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position / columnCount)
    }

    override fun onViewRecycled(holder: ContentViewHolder) =
        with(holder) {
            this.unbind()
            super.onViewRecycled(this)
        }
}