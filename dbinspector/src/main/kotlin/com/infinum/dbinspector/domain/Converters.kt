package com.infinum.dbinspector.domain

import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.proto.input.HistoryTask
import com.infinum.dbinspector.data.models.local.proto.input.SettingsTask
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.domain.shared.base.BaseConverter
import com.infinum.dbinspector.domain.shared.base.BaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SortParameters

internal interface Converters {

    interface Database : BaseConverter<DatabaseParameters, Operation> {

        suspend infix fun get(parameters: DatabaseParameters.Get): Operation

        suspend infix fun import(parameters: DatabaseParameters.Import): Operation

        suspend infix fun rename(parameters: DatabaseParameters.Rename): Operation

        suspend infix fun command(parameters: DatabaseParameters.Command): Operation
    }

    interface Connection : BaseConverter<ConnectionParameters, String>

    interface Sort : BaseConverter<SortParameters, Order>

    interface Pragma : BaseConverter<PragmaParameters, Query> {

        suspend infix fun version(parameters: PragmaParameters.Version): Query

        suspend infix fun pragma(parameters: PragmaParameters.Pragma): Query
    }

    interface Content : BaseConverter<ContentParameters, Query>

    interface BlobPreview : BaseConverter<SettingsParameters.BlobPreview, SettingsEntity.BlobPreviewMode>

    interface TruncateMode : BaseConverter<SettingsParameters.Truncate, SettingsEntity.TruncateMode>

    interface Settings : BaseConverter<SettingsParameters, SettingsTask> {

        suspend infix fun get(parameters: SettingsParameters.Get): SettingsTask

        suspend infix fun linesLimit(parameters: SettingsParameters.LinesLimit): SettingsTask

        suspend infix fun linesCount(parameters: SettingsParameters.LinesCount): SettingsTask

        suspend infix fun truncateMode(parameters: SettingsParameters.Truncate): SettingsTask

        suspend infix fun blobPreviewMode(parameters: SettingsParameters.BlobPreview): SettingsTask

        suspend infix fun ignoredTableName(parameters: SettingsParameters.IgnoredTableName): SettingsTask

        suspend infix fun serverPort(parameters: SettingsParameters.ServerPort): SettingsTask

        suspend infix fun startServer(parameters: SettingsParameters.StartServer): SettingsTask

        suspend infix fun stopServer(parameters: SettingsParameters.StopServer): SettingsTask
    }

    interface History : BaseConverter<HistoryParameters, HistoryTask> {

        infix fun get(parameters: HistoryParameters.All): HistoryTask

        suspend infix fun execution(parameters: HistoryParameters.Execution): HistoryTask

        suspend infix fun clear(parameters: HistoryParameters.All): HistoryTask
    }
}
