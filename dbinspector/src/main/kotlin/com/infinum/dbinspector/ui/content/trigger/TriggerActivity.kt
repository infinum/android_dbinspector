package com.infinum.dbinspector.ui.content.trigger

import com.infinum.dbinspector.R
import com.infinum.dbinspector.ui.content.shared.ContentActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TriggerActivity : ContentActivity() {

    override val viewModel: TriggerViewModel by viewModel()

    override val title: Int = R.string.dbinspector_trigger

    override val menu: Int = R.menu.dbinspector_trigger

    override val drop: Int = R.string.dbinspector_drop_trigger_confirm
}
