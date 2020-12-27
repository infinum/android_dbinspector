package com.infinum.dbinspector.ui.settings

import android.text.TextUtils
import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.ui.shared.base.BaseViewModel

internal class SettingsViewModel(
    private val loadAll: UseCases.LoadAllSettings,
    private val linesLimit: UseCases.ToggleLinesLimit,
    private val linesCount: UseCases.SaveLinesCount,
    private val truncateMode: UseCases.SaveTruncateMode,
    private val blobPreviewMode: UseCases.SaveBlobPreviewMode
) : BaseViewModel() {

    fun load(action: suspend (Settings) -> Unit) =
        launch {
            val result = io {
                loadAll(Unit)
            }
            action(result)
        }

    fun toggleLinesLimit(isEnabled: Boolean) {
        launch {
            io {
                linesLimit(isEnabled)
            }
        }
    }

    fun saveLinesCount(count: Int) {
        launch {
            io {
                linesCount(count)
            }
        }
    }

    fun saveTruncateMode(mode: TextUtils.TruncateAt) {
        launch {
            io {
                truncateMode(mode)
            }
        }
    }

    fun saveBlobPreviewType(type: BlobPreviewType) =
        launch {
            io {
                blobPreviewMode(type)
            }
        }
}
