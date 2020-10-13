package im.dino.dbinspector.domain

import android.database.sqlite.SQLiteDatabase
import im.dino.dbinspector.domain.database.models.DatabaseDescriptor
import im.dino.dbinspector.domain.database.models.Operation
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query

internal interface Repositories {

    interface Database {

        suspend fun getPage(operation: Operation): List<DatabaseDescriptor>

        suspend fun import(operation: Operation): List<DatabaseDescriptor>

        suspend fun remove(operation: Operation): List<DatabaseDescriptor>

        suspend fun rename(operation: Operation): List<DatabaseDescriptor>

        suspend fun copy(operation: Operation): List<DatabaseDescriptor>
    }

    interface Connection {

        suspend fun open(path: String): SQLiteDatabase

        suspend fun close(path: String)
    }

    interface Schema {

        suspend fun getPage(query: Query): Page

        suspend fun getByName(query: Query): Page

        suspend fun dropByName(query: Query): Page
    }

    interface Pragma {

        suspend fun getUserVersion(query: Query): Page

        suspend fun getTableInfo(query: Query): Page

        suspend fun getTriggerInfo(query: Query): Page

        suspend fun getForeignKeys(query: Query): Page

        suspend fun getIndexes(query: Query): Page
    }
}
