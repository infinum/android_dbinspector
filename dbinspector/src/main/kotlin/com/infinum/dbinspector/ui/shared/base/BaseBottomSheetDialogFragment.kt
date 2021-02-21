package com.infinum.dbinspector.ui.shared.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infinum.dbinspector.di.LibraryKoinComponent

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal abstract class BaseBottomSheetDialogFragment(
    @LayoutRes private val contentLayoutId: Int
) : BottomSheetDialogFragment(), LibraryKoinComponent {

    abstract val binding: ViewBinding

    abstract val viewModel: BaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(contentLayoutId, container, false)
}
