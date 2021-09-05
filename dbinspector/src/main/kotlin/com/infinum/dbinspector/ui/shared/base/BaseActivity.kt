package com.infinum.dbinspector.ui.shared.base

import android.os.Bundle
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
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
        ErrorDialog
            .setMessage(getString(R.string.dbinspector_error_parameters))
            .show(supportFragmentManager, null)
}
