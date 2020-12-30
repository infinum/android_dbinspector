package com.infinum.dbinspector.domain.settings.mappers

import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.models.TruncateMode

internal class TruncateModeMapper : Mappers.TruncateMode {

    override fun mapLocalToDomain(model: SettingsEntity.TruncateMode): TruncateMode =
        when (model.ordinal) {
            SettingsEntity.TruncateMode.START_VALUE -> TruncateMode.START
            SettingsEntity.TruncateMode.MIDDLE_VALUE -> TruncateMode.MIDDLE
            SettingsEntity.TruncateMode.END_VALUE -> TruncateMode.END
            else -> TruncateMode.END
        }

    override fun mapDomainToLocal(model: TruncateMode): SettingsEntity.TruncateMode =
        when (model) {
            TruncateMode.START -> SettingsEntity.TruncateMode.START
            TruncateMode.MIDDLE -> SettingsEntity.TruncateMode.MIDDLE
            TruncateMode.END -> SettingsEntity.TruncateMode.END
        }
}
