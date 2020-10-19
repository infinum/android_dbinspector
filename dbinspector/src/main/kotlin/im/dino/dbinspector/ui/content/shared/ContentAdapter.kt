package im.dino.dbinspector.ui.content.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import im.dino.dbinspector.databinding.DbinspectorItemCellBinding

internal class ContentAdapter(
    private val headersCount: Int
) : PagingDataAdapter<String, ContentViewHolder>(ContentDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder =
        ContentViewHolder(
            DbinspectorItemCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position / headersCount)
    }

    override fun onViewRecycled(holder: ContentViewHolder) =
        with(holder) {
            unbind()
            super.onViewRecycled(this)
        }
}
