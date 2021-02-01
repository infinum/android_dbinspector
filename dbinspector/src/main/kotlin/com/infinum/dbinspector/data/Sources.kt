package com.infinum.dbinspector.data

import android.database.sqlite.SQLiteDatabase
import androidx.datastore.core.DataStore
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import java.io.File

internal interface Sources {

    interface Raw {

        suspend fun getDatabases(operation: Operation): List<File>

        suspend fun importDatabases(operation: Operation): List<File>

        suspend fun removeDatabase(operation: Operation): List<File>

        suspend fun renameDatabase(operation: Operation): List<File>

        suspend fun copyDatabase(operation: Operation): List<File>
    }

    interface Memory {

        suspend fun openConnection(path: String): SQLiteDatabase

        suspend fun closeConnection(path: String)
    }

    interface Local {

        interface Store {

            suspend fun settings(): DataStore<SettingsEntity>

            suspend fun current(): SettingsEntity
        }

        interface Schema {

            suspend fun getTables(query: Query): QueryResult

            suspend fun getTableByName(query: Query): QueryResult

            suspend fun dropTableContentByName(query: Query): QueryResult

            suspend fun getViews(query: Query): QueryResult

            suspend fun getViewByName(query: Query): QueryResult

            suspend fun dropViewByName(query: Query): QueryResult

            suspend fun getTriggers(query: Query): QueryResult

            suspend fun getTriggerByName(query: Query): QueryResult

            suspend fun dropTriggerByName(query: Query): QueryResult
        }

        interface Pragma {

            suspend fun getUserVersion(query: Query): QueryResult

            suspend fun getTableInfo(query: Query): QueryResult

            suspend fun getForeignKeys(query: Query): QueryResult

            suspend fun getIndexes(query: Query): QueryResult
        }

        interface RawQuery {

            suspend fun rawQueryHeaders(query: Query): QueryResult

            suspend fun rawQuery(query: Query): QueryResult

            suspend fun affectedRows(query: Query): QueryResult
        }
    }
}
