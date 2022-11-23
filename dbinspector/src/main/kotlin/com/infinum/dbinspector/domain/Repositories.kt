package com.infinum.dbinspector.domain

import com.infinum.dbinspector.domain.connection.models.DatabaseConnection
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.history.models.History as HistoryModel
import com.infinum.dbinspector.domain.settings.models.Settings as SettingsModel
import com.infinum.dbinspector.domain.shared.base.BaseRepository
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.HistoryParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters
import kotlinx.coroutines.flow.Flow

internal interface Repositories {

    interface Database : BaseRepository<DatabaseParameters.Get, List<DatabaseDescriptor>> {

        suspend fun import(input: DatabaseParameters.Import): List<DatabaseDescriptor>

        suspend fun rename(input: DatabaseParameters.Rename): List<DatabaseDescriptor>

        suspend fun remove(input: DatabaseParameters.Command): List<DatabaseDescriptor>

        suspend fun copy(input: DatabaseParameters.Command): List<DatabaseDescriptor>
    }

    interface Connection {

        suspend fun open(input: ConnectionParameters): DatabaseConnection

        suspend fun close(input: ConnectionParameters)
    }

    interface Settings : BaseRepository<SettingsParameters.Get, SettingsModel> {

        suspend fun saveLinesLimit(input: SettingsParameters.LinesLimit)

        suspend fun saveLinesCount(input: SettingsParameters.LinesCount)

        suspend fun saveTruncateMode(input: SettingsParameters.Truncate)

        suspend fun saveBlobPreview(input: SettingsParameters.BlobPreview)

        suspend fun saveIgnoredTableName(input: SettingsParameters.IgnoredTableName)

        suspend fun removeIgnoredTableName(input: SettingsParameters.IgnoredTableName)

        suspend fun saveServerPort(input: SettingsParameters.ServerPort): SettingsModel

        suspend fun startServer(input: SettingsParameters.StartServer): SettingsModel

        suspend fun stopServer(input: SettingsParameters.StopServer): SettingsModel
    }

    interface History {

        fun getByDatabase(input: HistoryParameters.All): Flow<HistoryModel>

        suspend fun saveExecution(input: HistoryParameters.Execution)

        suspend fun clearByDatabase(input: HistoryParameters.All)

        suspend fun removeExecution(input: HistoryParameters.Execution)

        suspend fun getSimilarExecution(input: HistoryParameters.Execution): HistoryModel
    }

    interface Schema : BaseRepository<ContentParameters, Page> {

        suspend fun getByName(input: ContentParameters): Page

        suspend fun dropByName(input: ContentParameters): Page
    }

    interface Pragma {

        suspend fun getUserVersion(input: PragmaParameters.Version): Page

        suspend fun getTableInfo(input: PragmaParameters.Pragma): Page

        suspend fun getTriggerInfo(): Page

        suspend fun getForeignKeys(input: PragmaParameters.Pragma): Page

        suspend fun getIndexes(input: PragmaParameters.Pragma): Page
    }

    interface RawQuery : BaseRepository<ContentParameters, Page> {

        suspend fun getHeaders(input: ContentParameters): Page

        suspend fun getAffectedRows(input: ContentParameters): Page
    }
}
