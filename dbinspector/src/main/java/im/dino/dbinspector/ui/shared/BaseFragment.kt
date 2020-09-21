package im.dino.dbinspector.ui.shared

import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal abstract class BaseFragment(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId) {

    abstract val binding: ViewBinding
}
