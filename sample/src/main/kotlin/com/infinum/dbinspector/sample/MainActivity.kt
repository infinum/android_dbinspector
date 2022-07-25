package com.infinum.dbinspector.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinum.dbinspector.DbInspector
import com.infinum.dbinspector.sample.databinding.ActivityMainBinding
import com.infinum.dbinspector.ui.logger.AndroidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "${supportActionBar?.title?.toString().orEmpty()} (${BuildConfig.BUILD_TYPE})"

        ActivityMainBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }
            .also {
                it.show.setOnClickListener {
                    DbInspector.show(logger = AndroidLogger())
                }
            }

        viewModel.copy()
    }
}
