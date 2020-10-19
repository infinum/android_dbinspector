package im.dino.dbinspector.ui.shared.headers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.databinding.DbinspectorItemHeaderBinding

internal class HeaderAdapter(
    private val headerItems: List<String>
) : RecyclerView.Adapter<HeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder =
        HeaderViewHolder(
            DbinspectorItemHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        (holder as? HeaderViewHolder)?.bind(headerItems[position % headerItems.size])
    }

    override fun onViewRecycled(holder: HeaderViewHolder) =
        with(holder) {
            super.onViewRecycled(this)
        }

    override fun getItemCount(): Int {
        return headerItems.size
    }
}
