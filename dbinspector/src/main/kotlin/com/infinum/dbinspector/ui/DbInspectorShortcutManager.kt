package com.infinum.dbinspector.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import com.infinum.dbinspector.R
import com.infinum.dbinspector.extensions.queryIntentActivitiesCompat
import com.infinum.dbinspector.logger.AndroidLogger
import com.infinum.dbinspector.ui.databases.DatabasesActivity

@SuppressLint("StaticFieldLeak")
internal object DbInspectorShortcutManager {

    private const val ACTIVITY_INFO_NAME = "com.infinum.dbinspector.ui.databases.DatabasesActivity"

    private const val LAUNCHER_DYNAMIC_SHORTCUT_ID =
        "com.infinum.dbinspector.ui.dynamic_shortcut_launcher"

    fun init(context: Context) = addDynamicShortcut(context)

    @Suppress("LongMethod", "NestedBlockDepth")
    private fun addDynamicShortcut(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutManager: ShortcutManager =
                context.getSystemService(ShortcutManager::class.java)
            val dynamicShortcuts: MutableList<ShortcutInfo> = shortcutManager.dynamicShortcuts

            dynamicShortcuts
                .none { shortcut -> shortcut.id == LAUNCHER_DYNAMIC_SHORTCUT_ID }
                .takeIf { it }
                ?.let {
                    context.queryIntentActivitiesCompat(
                        Intent(Intent.ACTION_MAIN, null)
                            .apply {
                                addCategory(Intent.CATEGORY_LAUNCHER)
                                setPackage(context.packageName)
                            }
                    )
                        .filter { it.activityInfo.name != ACTIVITY_INFO_NAME }
                        .takeIf { it.isNotEmpty() }
                        ?.let { activities ->
                            val dbInspectorActivity = activities.first().activityInfo

                            val componentName = ComponentName(
                                dbInspectorActivity.packageName,
                                dbInspectorActivity.name
                            )

                            dynamicShortcuts.let {
                                it.count { shortcutInfo ->
                                    shortcutInfo.activity == componentName
                                } + shortcutManager.manifestShortcuts.count { shortcutInfo ->
                                    shortcutInfo.activity == componentName
                                }
                            }
                                .takeIf { it < shortcutManager.maxShortcutCountPerActivity }
                                ?.let {
                                    Presentation.setLogger(AndroidLogger())
                                    val intent = with(Presentation.applicationContext()) {
                                        Intent(
                                            this,
                                            DatabasesActivity::class.java
                                        ).apply {
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            action = "Dummy action because Android"
                                        }
                                    }

                                    ShortcutInfo.Builder(context, LAUNCHER_DYNAMIC_SHORTCUT_ID)
                                        .setLongLabel(context.getString(R.string.dbinspector_launcher_name))
                                        .setShortLabel(context.getString(R.string.dbinspector_launcher_name))
                                        .setActivity(componentName)
                                        .setIcon(
                                            Icon.createWithResource(
                                                context,
                                                R.mipmap.dbinspector_launcher
                                            )
                                        )
                                        .setIntent(intent)
                                        .build()
                                }
                        }
                        ?.let { shortcut ->
                            try {
                                shortcutManager.addDynamicShortcuts(listOf(shortcut))
                            } catch (ignored: Throwable) {
                                Unit
                            }
                        }
                }
        } else {
            Unit
        }
    }
}
