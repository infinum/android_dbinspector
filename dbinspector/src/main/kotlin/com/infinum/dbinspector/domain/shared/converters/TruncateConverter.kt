package com.infinum.dbinspector.domain.shared.converters

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.TruncateMode
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class TruncateConverter : Converters.Truncate {

    override suspend fun invoke(parameters: SettingsParameters.Truncate): SettingsEntity.TruncateMode =
        when (parameters.mode) {
            TruncateMode.START -> SettingsEntity.TruncateMode.START
            TruncateMode.MIDDLE -> SettingsEntity.TruncateMode.MIDDLE
            TruncateMode.END -> SettingsEntity.TruncateMode.END
            else -> SettingsEntity.TruncateMode.END
        }
}
