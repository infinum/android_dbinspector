package com.infinum.dbinspector.ui.settings

import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters
import com.infinum.dbinspector.ui.shared.base.BaseViewModel

@Suppress("LongParameterList")
internal class SettingsViewModel(
    private val getSettings: UseCases.GetSettings,
    private val saveIgnoredTableName: UseCases.SaveIgnoredTableName,
    private val removeIgnoredTableName: UseCases.RemoveIgnoredTableName,
    private val linesLimit: UseCases.ToggleLinesLimit,
    private val linesCount: UseCases.SaveLinesCount,
    private val truncateMode: UseCases.SaveTruncateMode,
    private val blobPreviewMode: UseCases.SaveBlobPreviewMode
) : BaseViewModel<SettingsState, SettingsEvent>() {

    fun load() =
        launch {
            val result = io {
                getSettings(SettingsParameters.Get())
            }
            setState(SettingsState.Settings(settings = result))
        }

    fun saveIgnoredTableName(newName: String) =
        launch {
            val result: String = io {
                saveIgnoredTableName(SettingsParameters.IgnoredTableName(newName))
                newName
            }
            emitEvent(SettingsEvent.AddIgnoredTable(name = result))
        }

    fun removeIgnoredTableName(newName: String) =
        launch {
            val result = io {
                removeIgnoredTableName(SettingsParameters.IgnoredTableName(newName))
                newName
            }
            emitEvent(SettingsEvent.RemoveIgnoredTable(name = result))
        }

    fun toggleLinesLimit(isEnabled: Boolean) {
        launch {
            io {
                linesLimit(SettingsParameters.LinesLimit(isEnabled))
            }
        }
    }

    fun saveLinesCount(count: Int) {
        launch {
            io {
                linesCount(SettingsParameters.LinesCount(count))
            }
        }
    }

    fun saveTruncateMode(mode: TruncateMode) {
        launch {
            io {
                truncateMode(SettingsParameters.Truncate(mode))
            }
        }
    }

    fun saveBlobPreviewType(mode: BlobPreviewMode) =
        launch {
            io {
                blobPreviewMode(SettingsParameters.BlobPreview(mode))
            }
        }
}
