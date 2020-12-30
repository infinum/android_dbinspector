package com.infinum.dbinspector.domain

import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.shared.base.BaseMapper
import com.infinum.dbinspector.domain.settings.models.Settings as SettingsModel
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode as BlobPreviewModeModel
import com.infinum.dbinspector.domain.shared.models.Cell as CellModel
import com.infinum.dbinspector.domain.shared.models.Page as PageModel
import com.infinum.dbinspector.domain.shared.models.Sort as SortModel
import com.infinum.dbinspector.domain.shared.models.TruncateMode as TruncateModeModel

internal interface Mappers {

    interface Cell : BaseMapper<Field, CellModel>

    interface Sort : BaseMapper<Order, SortModel>

    interface Page : BaseMapper<QueryResult, PageModel> {

        fun sortMapper(): BaseMapper<Order, SortModel>

        fun blobPreviewModeMapper(): BaseMapper<SettingsEntity.BlobPreviewMode, BlobPreviewModeModel>
    }

    interface Pragma : BaseMapper<QueryResult, PageModel> {

        fun sortMapper(): BaseMapper<Order, SortModel>

        fun transformToHeader(): ((String) -> CellModel)

        fun transformToCell(): ((Field) -> CellModel)
    }

    interface TruncateMode : BaseMapper<SettingsEntity.TruncateMode, TruncateModeModel>

    interface BlobPreviewMode : BaseMapper<SettingsEntity.BlobPreviewMode, BlobPreviewModeModel>

    interface Settings : BaseMapper<SettingsEntity, SettingsModel> {

        fun truncateModeMapper(): BaseMapper<SettingsEntity.TruncateMode, TruncateModeModel>

        fun blobPreviewModeMapper(): BaseMapper<SettingsEntity.BlobPreviewMode, BlobPreviewModeModel>
    }
}
