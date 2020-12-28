package com.infinum.dbinspector.domain

import android.text.TextUtils
import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.domain.settings.models.Settings
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Query
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SchemaParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal interface UseCases {

    // region Database
    interface GetDatabases : BaseUseCase<DatabaseParameters.Get, List<DatabaseDescriptor>>

    interface ImportDatabases : BaseUseCase<DatabaseParameters.Import, List<DatabaseDescriptor>>

    interface RemoveDatabase : BaseUseCase<DatabaseParameters.Remove, List<DatabaseDescriptor>>

    interface RenameDatabase : BaseUseCase<DatabaseParameters.Rename, List<DatabaseDescriptor>>

    interface CopyDatabase : BaseUseCase<DatabaseParameters.Copy, List<DatabaseDescriptor>>
    // endregion

    // region Connection
    interface OpenConnection : BaseUseCase<ConnectionParameters, Unit>

    interface CloseConnection : BaseUseCase<ConnectionParameters, Unit>
    // endregion

    // region Settings
    interface GetSettings : BaseUseCase<SettingsParameters.Get, Settings>

    interface ToggleLinesLimit : BaseUseCase<SettingsParameters.LinesLimit, Unit>

    interface SaveLinesCount : BaseUseCase<SettingsParameters.LinesCount, Unit>

    interface SaveTruncateMode : BaseUseCase<SettingsParameters.TruncateMode, Unit>

    interface SaveBlobPreviewMode : BaseUseCase<SettingsParameters.BlobPreviewMode, Unit>
    // endregion

    // region Schema
    interface GetTables : BaseUseCase<ContentParameters, Page>

    interface GetViews : BaseUseCase<ContentParameters, Page>

    interface GetTriggers : BaseUseCase<ContentParameters, Page>
    // endregion

    // region Content
    interface GetTable : BaseUseCase<ContentParameters, Page>

    interface GetView : BaseUseCase<ContentParameters, Page>

    interface GetTrigger : BaseUseCase<ContentParameters, Page>

    interface DropTableContent : BaseUseCase<ContentParameters, Page>

    interface DropView : BaseUseCase<ContentParameters, Page>

    interface DropTrigger : BaseUseCase<ContentParameters, Page>
    // endregion

    // region Pragma
    interface GetTableInfo : BaseUseCase<PragmaParameters.Info, Page>

    interface GetTriggerInfo : BaseUseCase<PragmaParameters.Info, Page>

    interface GetTablePragma : BaseUseCase<PragmaParameters.Info, Page>

    interface GetForeignKeys : BaseUseCase<PragmaParameters.ForeignKeys, Page>

    interface GetIndexes : BaseUseCase<PragmaParameters.Indexes, Page>
    // endregion
}
