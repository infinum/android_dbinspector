package com.infinum.dbinspector.domain

import com.infinum.dbinspector.domain.connection.models.DatabaseConnection
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.settings.models.Settings as SettingsModel
import com.infinum.dbinspector.domain.shared.base.BaseRepository
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.DatabaseParameters
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

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
    }

    interface Schema : BaseRepository<ContentParameters, Page> {

        suspend fun getByName(input: ContentParameters): Page

        suspend fun dropByName(input: ContentParameters): Page
    }

    interface Pragma {

        suspend fun getUserVersion(input: PragmaParameters.Version): Page

        suspend fun getTableInfo(input: PragmaParameters.Pragma): Page

        suspend fun getTriggerInfo(input: Unit): Page

        suspend fun getForeignKeys(input: PragmaParameters.Pragma): Page

        suspend fun getIndexes(input: PragmaParameters.Pragma): Page
    }

    interface RawQuery : BaseRepository<ContentParameters, Page> {

        suspend fun getHeaders(input: ContentParameters): Page

        suspend fun getAffectedRows(input: ContentParameters): Page
    }
}
