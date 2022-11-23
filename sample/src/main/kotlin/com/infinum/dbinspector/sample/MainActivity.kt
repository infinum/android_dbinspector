package com.infinum.dbinspector.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinum.dbinspector.DbInspector
import com.infinum.dbinspector.logger.AndroidLogger
import com.infinum.dbinspector.sample.databinding.ActivityMainBinding
import java.net.Inet4Address
import java.net.NetworkInterface
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title =
            "${supportActionBar?.title?.toString().orEmpty()} (${BuildConfig.BUILD_TYPE})"
        supportActionBar?.subtitle = address()?.let { "IPv4 address: $it" }

        ActivityMainBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }
            .also {
                it.show.setOnClickListener {
                    DbInspector.show(logger = AndroidLogger())
                }
            }

        viewModel.copy()
    }

    private fun address(): String? =
        NetworkInterface
            .getNetworkInterfaces()
            ?.toList()
            ?.map { networkInterface ->
                networkInterface
                    .inetAddresses
                    ?.toList()
                    ?.find { !it.isLoopbackAddress && it is Inet4Address }
                    ?.hostAddress
            }
            ?.firstOrNull()
}
