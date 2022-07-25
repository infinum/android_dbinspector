package com.infinum.dbinspector.data

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.data.models.raw.OperationTask
import com.infinum.dbinspector.data.shared.base.BaseStore
import java.io.File

public interface Sources {

    public interface Raw {

        public suspend fun getDatabases(operation: OperationTask): List<File>

        public suspend fun importDatabases(operation: OperationTask): List<File>

        public suspend fun removeDatabase(operation: OperationTask): List<File>

        public suspend fun renameDatabase(operation: OperationTask): List<File>

        public suspend fun copyDatabase(operation: OperationTask): List<File>
    }

    public interface Memory {

        public interface Connection {

            public suspend fun openConnection(path: String): SQLiteDatabase

            public suspend fun closeConnection(path: String)
        }

        public interface Distance {

            public suspend fun calculate(query: String, options: List<String>): Int?
        }
    }

    public interface Local {

        public interface Settings : BaseStore<SettingsEntity>

        public interface History : BaseStore<HistoryEntity>

        public interface Schema {

            public suspend fun getTables(query: Query): QueryResult

            public suspend fun getTableByName(query: Query): QueryResult

            public suspend fun dropTableContentByName(query: Query): QueryResult

            public suspend fun getViews(query: Query): QueryResult

            public suspend fun getViewByName(query: Query): QueryResult

            public suspend fun dropViewByName(query: Query): QueryResult

            public suspend fun getTriggers(query: Query): QueryResult

            public suspend fun getTriggerByName(query: Query): QueryResult

            public suspend fun dropTriggerByName(query: Query): QueryResult
        }

        public interface Pragma {

            public suspend fun getUserVersion(query: Query): QueryResult

            public suspend fun getTableInfo(query: Query): QueryResult

            public suspend fun getForeignKeys(query: Query): QueryResult

            public suspend fun getIndexes(query: Query): QueryResult
        }

        public interface RawQuery {

            public suspend fun rawQueryHeaders(query: Query): QueryResult

            public suspend fun rawQuery(query: Query): QueryResult

            public suspend fun affectedRows(query: Query): QueryResult
        }
    }
}
