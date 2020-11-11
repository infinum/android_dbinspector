package com.infinum.dbinspector.ui.content.table

import com.infinum.dbinspector.R
import com.infinum.dbinspector.ui.content.shared.ContentActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TableActivity : ContentActivity() {

    override val viewModel: TableViewModel by viewModel()

    override val title: Int = R.string.dbinspector_table

    override val menu: Int = R.menu.dbinspector_table

    override val drop: Int = R.string.dbinspector_clear_table_confirm
}
