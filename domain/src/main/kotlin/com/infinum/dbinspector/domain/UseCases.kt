package com.infinum.dbinspector.domain

import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.history.models.History
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.domain.shared.base.BaseFlowUseCase
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters
import kotlinx.coroutines.flow.Flow

public interface UseCases {

    // region Database
    public interface GetDatabases : BaseUseCase<DatabaseParameters.Get, List<DatabaseDescriptor>>

    public interface ImportDatabases : BaseUseCase<DatabaseParameters.Import, List<DatabaseDescriptor>>

    public interface RemoveDatabase : BaseUseCase<DatabaseParameters.Command, List<DatabaseDescriptor>>

    public interface RenameDatabase : BaseUseCase<DatabaseParameters.Rename, List<DatabaseDescriptor>>

    public interface CopyDatabase : BaseUseCase<DatabaseParameters.Command, List<DatabaseDescriptor>>
    // endregion

    // region Connection
    public interface OpenConnection : BaseUseCase<ConnectionParameters, Unit>

    public interface CloseConnection : BaseUseCase<ConnectionParameters, Unit>
    // endregion

    // region Settings
    public interface GetSettings : BaseUseCase<SettingsParameters.Get, Settings>

    public interface SaveIgnoredTableName : BaseUseCase<SettingsParameters.IgnoredTableName, Unit>

    public interface RemoveIgnoredTableName : BaseUseCase<SettingsParameters.IgnoredTableName, Unit>

    public interface ToggleLinesLimit : BaseUseCase<SettingsParameters.LinesLimit, Unit>

    public interface SaveLinesCount : BaseUseCase<SettingsParameters.LinesCount, Unit>

    public interface SaveTruncateMode : BaseUseCase<SettingsParameters.Truncate, Unit>

    public interface SaveBlobPreviewMode : BaseUseCase<SettingsParameters.BlobPreview, Unit>
    // endregion

    // region Schema
    public interface GetTables : BaseUseCase<ContentParameters, Page>

    public interface GetViews : BaseUseCase<ContentParameters, Page>

    public interface GetTriggers : BaseUseCase<ContentParameters, Page>
    // endregion

    // region Content
    public interface GetTable : BaseUseCase<ContentParameters, Page>

    public interface GetView : BaseUseCase<ContentParameters, Page>

    public interface GetTrigger : BaseUseCase<ContentParameters, Page>

    public interface DropTableContent : BaseUseCase<ContentParameters, Page>

    public interface DropView : BaseUseCase<ContentParameters, Page>

    public interface DropTrigger : BaseUseCase<ContentParameters, Page>

    public interface GetRawQueryHeaders : BaseUseCase<ContentParameters, Page>

    public interface GetRawQuery : BaseUseCase<ContentParameters, Page>

    public interface GetAffectedRows : BaseUseCase<ContentParameters, Page>
    // endregion

    // region Pragma
    public interface GetTableInfo : BaseUseCase<PragmaParameters.Pragma, Page>

    public interface GetTriggerInfo : BaseUseCase<PragmaParameters.Pragma, Page>

    public interface GetTablePragma : BaseUseCase<PragmaParameters.Pragma, Page>

    public interface GetForeignKeys : BaseUseCase<PragmaParameters.Pragma, Page>

    public interface GetIndexes : BaseUseCase<PragmaParameters.Pragma, Page>
    // endregion

    // region Settings
    public interface GetHistory : BaseFlowUseCase<HistoryParameters.All, Flow<History>>

    public interface SaveExecution : BaseUseCase<HistoryParameters.Execution, Unit>

    public interface ClearHistory : BaseUseCase<HistoryParameters.All, Unit>

    public interface RemoveExecution : BaseUseCase<HistoryParameters.Execution, Unit>

    public interface GetSimilarExecution : BaseUseCase<HistoryParameters.Execution, History>
    // endregion
}
