package com.infinum.dbinspector.ui.shared.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.infinum.dbinspector.di.LibraryKoinComponent

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal abstract class BaseFragment<State, Event>(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId), BaseView<State, Event>, LibraryKoinComponent {

    abstract val binding: ViewBinding

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        collectFlows(viewLifecycleOwner)
    }
}
