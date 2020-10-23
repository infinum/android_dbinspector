package com.infinum.dbinspector.ui.shared.base

import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.infinum.dbinspector.ui.DbInspectorKoinComponent

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal abstract class BaseFragment(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId), DbInspectorKoinComponent {

    abstract val binding: ViewBinding
}
