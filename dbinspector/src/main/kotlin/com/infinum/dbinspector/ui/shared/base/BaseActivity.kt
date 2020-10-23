package com.infinum.dbinspector.ui.shared.base

import android.os.Bundle
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.infinum.dbinspector.ui.DbInspectorKoinComponent

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal abstract class BaseActivity : AppCompatActivity(), DbInspectorKoinComponent {

    abstract val binding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
    }
}
