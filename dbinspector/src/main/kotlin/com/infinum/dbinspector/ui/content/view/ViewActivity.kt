package com.infinum.dbinspector.ui.content.view

import com.infinum.dbinspector.R
import com.infinum.dbinspector.ui.content.shared.ContentActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class ViewActivity : ContentActivity() {

    override val viewModel: ViewViewModel by viewModel()

    override val title: Int = R.string.dbinspector_view

    override val menu: Int = R.menu.dbinspector_view

    override val drop: Int = R.string.dbinspector_drop_view_confirm
}
