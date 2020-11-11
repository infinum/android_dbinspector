package com.infinum.dbinspector.domain

import android.database.sqlite.SQLiteDatabase
import com.infinum.dbinspector.domain.database.models.DatabaseDescriptor
import com.infinum.dbinspector.domain.database.models.Operation
import com.infinum.dbinspector.domain.shared.base.BaseRepository
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Query

internal interface Repositories {

    interface Database : BaseRepository<Operation, List<DatabaseDescriptor>> {

        suspend fun import(operation: Operation): List<DatabaseDescriptor>

        suspend fun remove(operation: Operation): List<DatabaseDescriptor>

        suspend fun rename(operation: Operation): List<DatabaseDescriptor>

        suspend fun copy(operation: Operation): List<DatabaseDescriptor>
    }

    interface Connection {

        suspend fun open(path: String): SQLiteDatabase

        suspend fun close(path: String)
    }

    interface Schema : BaseRepository<Query, Page> {

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
