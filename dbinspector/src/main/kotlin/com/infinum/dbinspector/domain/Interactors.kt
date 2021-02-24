package com.infinum.dbinspector.domain

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.domain.shared.base.BaseFlowInteractor
import com.infinum.dbinspector.domain.shared.base.BaseInteractor
import java.io.File
import kotlinx.coroutines.flow.Flow

internal interface Interactors {

    // region Database
    interface GetDatabases : BaseInteractor<Operation, List<File>>

    interface ImportDatabases : BaseInteractor<Operation, List<File>>

    interface RemoveDatabase : BaseInteractor<Operation, List<File>>

    interface RenameDatabase : BaseInteractor<Operation, List<File>>

    interface CopyDatabase : BaseInteractor<Operation, List<File>>
    // endregion

    // region Connection
    interface OpenConnection : BaseInteractor<String, SQLiteDatabase>

    interface CloseConnection : BaseInteractor<String, Unit>
    // endregion

    // region Settings
    interface GetSettings : BaseInteractor<SettingsTask, SettingsEntity>

    interface SaveLinesLimit : BaseInteractor<SettingsTask, Unit>

    interface SaveLinesCount : BaseInteractor<SettingsTask, Unit>

    interface SaveTruncateMode : BaseInteractor<SettingsTask, Unit>

    interface SaveBlobPreviewMode : BaseInteractor<SettingsTask, Unit>

    interface SaveIgnoredTableName : BaseInteractor<SettingsTask, Unit>

    interface RemoveIgnoredTableName : BaseInteractor<SettingsTask, Unit>
    // endregion

    // region Schema
    interface GetTables : BaseInteractor<Query, QueryResult>

    interface GetTableByName : BaseInteractor<Query, QueryResult>

    interface DropTableContentByName : BaseInteractor<Query, QueryResult>

    interface GetTriggers : BaseInteractor<Query, QueryResult>

    interface GetTriggerByName : BaseInteractor<Query, QueryResult>

    interface DropTriggerByName : BaseInteractor<Query, QueryResult>

    interface GetViews : BaseInteractor<Query, QueryResult>

    interface GetViewByName : BaseInteractor<Query, QueryResult>

    interface DropViewByName : BaseInteractor<Query, QueryResult>

    interface GetRawQueryHeaders : BaseInteractor<Query, QueryResult>

    interface GetRawQuery : BaseInteractor<Query, QueryResult>

    interface GetAffectedRows : BaseInteractor<Query, QueryResult>
    // endregion

    // region Pragma
    interface GetUserVersion : BaseInteractor<Query, QueryResult>

    interface GetTableInfo : BaseInteractor<Query, QueryResult>

    interface GetForeignKeys : BaseInteractor<Query, QueryResult>

    interface GetIndexes : BaseInteractor<Query, QueryResult>
    // endregion

    // region History
    interface GetHistory : BaseFlowInteractor<HistoryTask, Flow<HistoryEntity>>

    interface SaveExecution : BaseInteractor<HistoryTask, Unit>

    interface ClearHistory : BaseInteractor<HistoryTask, Unit>

    interface RemoveExecution : BaseInteractor<HistoryTask, Unit>

    interface GetExecution : BaseInteractor<HistoryTask, HistoryEntity>
    // endregion
}
