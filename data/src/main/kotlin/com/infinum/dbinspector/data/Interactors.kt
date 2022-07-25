package com.infinum.dbinspector.data

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.data.models.raw.OperationTask
import com.infinum.dbinspector.data.shared.base.BaseFlowInteractor
import com.infinum.dbinspector.data.shared.base.BaseInteractor
import java.io.File
import kotlinx.coroutines.flow.Flow

public interface Interactors {

    // region Database
    public interface GetDatabases : BaseInteractor<OperationTask, List<File>>

    public interface ImportDatabases : BaseInteractor<OperationTask, List<File>>

    public interface RemoveDatabase : BaseInteractor<OperationTask, List<File>>

    public interface RenameDatabase : BaseInteractor<OperationTask, List<File>>

    public interface CopyDatabase : BaseInteractor<OperationTask, List<File>>
    // endregion

    // region Connection
    public interface OpenConnection : BaseInteractor<String, SQLiteDatabase>

    public interface CloseConnection : BaseInteractor<String, Unit>
    // endregion

    // region Settings
    public interface GetSettings : BaseInteractor<SettingsTask, SettingsEntity>

    public interface SaveLinesLimit : BaseInteractor<SettingsTask, Unit>

    public interface SaveLinesCount : BaseInteractor<SettingsTask, Unit>

    public interface SaveTruncateMode : BaseInteractor<SettingsTask, Unit>

    public interface SaveBlobPreviewMode : BaseInteractor<SettingsTask, Unit>

    public interface SaveIgnoredTableName : BaseInteractor<SettingsTask, Unit>

    public interface RemoveIgnoredTableName : BaseInteractor<SettingsTask, Unit>
    // endregion

    // region Schema
    public interface GetTables : BaseInteractor<Query, QueryResult>

    public interface GetTableByName : BaseInteractor<Query, QueryResult>

    public interface DropTableContentByName : BaseInteractor<Query, QueryResult>

    public interface GetTriggers : BaseInteractor<Query, QueryResult>

    public interface GetTriggerByName : BaseInteractor<Query, QueryResult>

    public interface DropTriggerByName : BaseInteractor<Query, QueryResult>

    public interface GetViews : BaseInteractor<Query, QueryResult>

    public interface GetViewByName : BaseInteractor<Query, QueryResult>

    public interface DropViewByName : BaseInteractor<Query, QueryResult>

    public interface GetRawQueryHeaders : BaseInteractor<Query, QueryResult>

    public interface GetRawQuery : BaseInteractor<Query, QueryResult>

    public interface GetAffectedRows : BaseInteractor<Query, QueryResult>
    // endregion

    // region Pragma
    public interface GetUserVersion : BaseInteractor<Query, QueryResult>

    public interface GetTableInfo : BaseInteractor<Query, QueryResult>

    public interface GetForeignKeys : BaseInteractor<Query, QueryResult>

    public interface GetIndexes : BaseInteractor<Query, QueryResult>
    // endregion

    // region History
    public interface GetHistory : BaseFlowInteractor<HistoryTask, Flow<HistoryEntity>>

    public interface SaveExecution : BaseInteractor<HistoryTask, Unit>

    public interface ClearHistory : BaseInteractor<HistoryTask, Unit>

    public interface RemoveExecution : BaseInteractor<HistoryTask, Unit>

    public interface GetExecution : BaseInteractor<HistoryTask, HistoryEntity>
    // endregion
}
