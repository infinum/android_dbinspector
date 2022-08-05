package com.infinum.dbinspector.ui.shared.delegates

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.infinum.dbinspector.ui.Presentation
import com.infinum.dbinspector.ui.shared.base.lifecycle.LifecycleConnection
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class LifecycleConnectionDelegate(
    private val owner: LifecycleOwner,
    val lifecycleConnectionFactory: (Bundle?) -> LifecycleConnection
) : ReadOnlyProperty<LifecycleOwner, LifecycleConnection>, LifecycleObserver {

    private var connection: LifecycleConnection? = null

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): LifecycleConnection {
        val existingConnection = connection

        return when {
            existingConnection != null -> existingConnection
            else -> {
                if (
                    owner
                        .lifecycle
                        .currentState
                        .isAtLeast(Lifecycle.State.INITIALIZED).not()
                ) {
                    error("Owner has not passed created yet.")
                }

                val extras = when (thisRef) {
                    is Fragment -> (thisRef as? Fragment)?.activity?.intent?.extras
                    is AppCompatActivity -> thisRef.intent?.extras
                    else -> null
                }

                lifecycleConnectionFactory(extras).also { this.connection = it }
            }
        }
    }
}

internal fun LifecycleOwner.lifecycleConnection(
    lifecycleConnectionFactory: (Bundle?) -> LifecycleConnection = {
        LifecycleConnection(
            databaseName = it?.getString(Presentation.Constants.Keys.DATABASE_NAME),
            databasePath = it?.getString(Presentation.Constants.Keys.DATABASE_PATH),
            schemaName = it?.getString(Presentation.Constants.Keys.SCHEMA_NAME)
        )
    }
) =
    LifecycleConnectionDelegate(this, lifecycleConnectionFactory)
