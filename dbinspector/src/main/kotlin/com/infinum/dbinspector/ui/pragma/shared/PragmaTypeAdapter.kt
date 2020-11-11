package com.infinum.dbinspector.ui.pragma.shared

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infinum.dbinspector.domain.pragma.models.PragmaType
import com.infinum.dbinspector.ui.pragma.foreignkeys.ForeignKeysFragment
import com.infinum.dbinspector.ui.pragma.indexes.IndexesFragment
import com.infinum.dbinspector.ui.pragma.tableinfo.TableInfoFragment

internal class PragmaTypeAdapter(
    fragmentActivity: FragmentActivity,
    private val databasePath: String,
    private val tableName: String
) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment =
        when (PragmaType.values()[position]) {
            PragmaType.TABLE_INFO -> TableInfoFragment.newInstance(databasePath, tableName)
            PragmaType.FOREIGN_KEY -> ForeignKeysFragment.newInstance(databasePath, tableName)
            PragmaType.INDEX -> IndexesFragment.newInstance(databasePath, tableName)
        }

    override fun getItemCount(): Int = PragmaType.values().size
}
