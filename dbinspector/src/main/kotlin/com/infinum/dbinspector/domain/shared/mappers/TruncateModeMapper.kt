package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.TruncateMode

internal class TruncateModeMapper : Mappers.TruncateMode {

    override suspend fun invoke(model: SettingsEntity.TruncateMode): TruncateMode =
        when (model.ordinal) {
            SettingsEntity.TruncateMode.START_VALUE -> TruncateMode.START
            SettingsEntity.TruncateMode.MIDDLE_VALUE -> TruncateMode.MIDDLE
            SettingsEntity.TruncateMode.END_VALUE -> TruncateMode.END
            else -> TruncateMode.END
        }
}
