package com.infinum.dbinspector.domain

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.data.models.local.cursor.output.Field
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.connection.models.DatabaseConnection
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor as DatabaseDescriptorModel
import com.infinum.dbinspector.domain.history.models.Execution as ExecutionModel
import com.infinum.dbinspector.domain.history.models.History as HistoryModel
import com.infinum.dbinspector.domain.settings.models.Settings as SettingsModel
import com.infinum.dbinspector.domain.shared.base.BaseMapper
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode as BlobPreviewModeModel
import com.infinum.dbinspector.domain.shared.models.Cell as CellModel
import com.infinum.dbinspector.domain.shared.models.Page as PageModel
import com.infinum.dbinspector.domain.shared.models.TruncateMode as TruncateModeModel
import java.io.File

internal interface Mappers {

    interface Connection : BaseMapper<SQLiteDatabase, DatabaseConnection>

    interface Database : BaseMapper<File, DatabaseDescriptorModel>

    interface Cell : BaseMapper<Field, CellModel>

    interface Content : BaseMapper<QueryResult, PageModel>

    interface Pragma : BaseMapper<QueryResult, PageModel> {

        fun transformToHeader(): ((String) -> CellModel)

        fun transformToCell(): ((Field) -> CellModel)
    }

    interface TruncateMode : BaseMapper<SettingsEntity.TruncateMode, TruncateModeModel>

    interface BlobPreviewMode : BaseMapper<SettingsEntity.BlobPreviewMode, BlobPreviewModeModel>

    interface Settings : BaseMapper<SettingsEntity, SettingsModel>

    interface History : BaseMapper<HistoryEntity, HistoryModel>

    interface Execution : BaseMapper<HistoryEntity.ExecutionEntity, ExecutionModel>
}
