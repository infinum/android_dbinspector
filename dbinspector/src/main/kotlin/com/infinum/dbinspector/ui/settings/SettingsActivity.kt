package com.infinum.dbinspector.ui.settings

import android.os.Bundle
import com.infinum.dbinspector.databinding.DbinspectorActivitySettingsBinding
import com.infinum.dbinspector.ui.shared.base.BaseActivity
import com.infinum.dbinspector.ui.shared.delegates.viewBinding

internal class SettingsActivity : BaseActivity() {

    override val binding by viewBinding(DbinspectorActivitySettingsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()
    }

    private fun setupUi() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }
}
