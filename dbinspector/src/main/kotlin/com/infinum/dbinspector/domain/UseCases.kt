package com.infinum.dbinspector.domain

import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Query

internal interface UseCases {

    // region Database
    interface GetDatabases : BaseUseCase<Operation, List<DatabaseDescriptor>>

    interface ImportDatabases : BaseUseCase<Operation, List<DatabaseDescriptor>>

    interface RemoveDatabase : BaseUseCase<Operation, List<DatabaseDescriptor>>

    interface RenameDatabase : BaseUseCase<Operation, List<DatabaseDescriptor>>

    interface CopyDatabase : BaseUseCase<Operation, List<DatabaseDescriptor>>
    // endregion

    // region Connection
    interface OpenConnection : BaseUseCase<String, Unit>

    interface CloseConnection : BaseUseCase<String, Unit>
    // endregion

    // region Schema
    interface GetTables : BaseUseCase<Query, Page>

    interface GetViews : BaseUseCase<Query, Page>

    interface GetTriggers : BaseUseCase<Query, Page>
    // endregion

    // region Content
    interface GetTableInfo : BaseUseCase<Query, Page>

    interface GetTriggerInfo : BaseUseCase<Query, Page>

    interface GetTable : BaseUseCase<Query, Page>

    interface GetView : BaseUseCase<Query, Page>

    interface GetTrigger : BaseUseCase<Query, Page>

    interface DropTableContent : BaseUseCase<Query, Page>

    interface DropView : BaseUseCase<Query, Page>

    interface DropTrigger : BaseUseCase<Query, Page>
    // endregion

    // region Pragma
    interface GetTablePragma : BaseUseCase<Query, Page>

    interface GetForeignKeys : BaseUseCase<Query, Page>

    interface GetIndexes : BaseUseCase<Query, Page>
    // endregion
}
