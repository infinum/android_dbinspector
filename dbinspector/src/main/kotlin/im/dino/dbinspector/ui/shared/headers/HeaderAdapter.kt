package im.dino.dbinspector.ui.shared.headers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import im.dino.dbinspector.databinding.DbinspectorItemHeaderBinding
import im.dino.dbinspector.domain.shared.models.Direction

internal class HeaderAdapter(
    private val headerItems: List<Header>,
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
}
