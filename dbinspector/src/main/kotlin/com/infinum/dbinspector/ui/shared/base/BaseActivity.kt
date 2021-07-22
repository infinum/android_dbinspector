package com.infinum.dbinspector.ui.shared.base

import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.infinum.dbinspector.R
import com.infinum.dbinspector.di.LibraryKoinComponent

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal abstract class BaseActivity<State, Event> : AppCompatActivity(), BaseView<State, Event>, LibraryKoinComponent {

    abstract val binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        collectFlows(lifecycleScope)
    }

    fun showDatabaseParametersError() =
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(R.string.dbinspector_title_error)
            .setMessage(R.string.dbinspector_error_parameters)
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                finish()
            }
            .create()
            .show()
}
