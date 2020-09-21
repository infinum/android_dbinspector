package im.dino.dbinspector.ui.tables.pragma

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import im.dino.dbinspector.databinding.DbinspectorItemCellBinding

class PragmaAdapter(
    private val rowCount: Int
) : PagingDataAdapter<String, PragmaViewHolder>(PragmaDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PragmaViewHolder =
        PragmaViewHolder(DbinspectorItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PragmaViewHolder, position: Int) =
        holder.bind(getItem(position), position / rowCount)
}