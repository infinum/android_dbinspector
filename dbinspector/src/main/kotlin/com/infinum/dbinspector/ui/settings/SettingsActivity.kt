package com.infinum.dbinspector.ui.settings

import com.infinum.dbinspector.databinding.DbinspectorActivitySettingsBinding
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding

internal class SettingsActivity : BaseActivity() {

    override val binding by viewBinding(DbinspectorActivitySettingsBinding::inflate)
}
