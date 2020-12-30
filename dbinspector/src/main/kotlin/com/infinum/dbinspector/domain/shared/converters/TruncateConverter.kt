package com.infinum.dbinspector.domain.shared.converters

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.TruncateMode

internal class TruncateConverter : Converters.Truncate {

    override suspend fun invoke(parameters: TruncateMode): SettingsEntity.TruncateMode =
        when (parameters) {
            TruncateMode.START -> SettingsEntity.TruncateMode.START
            TruncateMode.MIDDLE -> SettingsEntity.TruncateMode.MIDDLE
            TruncateMode.END -> SettingsEntity.TruncateMode.END
        }
}
