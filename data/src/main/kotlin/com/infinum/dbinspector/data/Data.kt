package com.infinum.dbinspector.data

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import com.infinum.dbinspector.data.Data.Constants.Name.PROTO_FILENAME_HISTORY
import com.infinum.dbinspector.data.Data.Constants.Name.PROTO_FILENAME_SETTINGS
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.data.sources.local.cursor.PragmaSource
import com.infinum.dbinspector.data.sources.local.cursor.RawQuerySource
import com.infinum.dbinspector.data.sources.local.cursor.SchemaSource
import com.infinum.dbinspector.data.sources.local.proto.history.HistoryDataStore
import com.infinum.dbinspector.data.sources.local.proto.history.HistorySerializer
import com.infinum.dbinspector.data.sources.local.proto.settings.SettingsDataStore
import com.infinum.dbinspector.data.sources.local.proto.settings.SettingsSerializer
import com.infinum.dbinspector.data.sources.memory.connection.AndroidConnectionSource
import com.infinum.dbinspector.data.sources.memory.distance.LevenshteinDistance
import com.infinum.dbinspector.data.sources.memory.pagination.CursorPaginator
import com.infinum.dbinspector.data.sources.memory.pagination.Paginator
import com.infinum.dbinspector.data.sources.raw.AndroidDatabasesSource
import com.infinum.dbinspector.data.extensions.dataStoreFile
import com.infinum.dbinspector.data.interactors.connection.CloseConnectionInteractor
import com.infinum.dbinspector.data.interactors.connection.OpenConnectionInteractor
import com.infinum.dbinspector.data.interactors.database.CopyDatabaseInteractor
import com.infinum.dbinspector.data.interactors.database.GetDatabasesInteractor
import com.infinum.dbinspector.data.interactors.database.ImportDatabasesInteractor
import com.infinum.dbinspector.data.interactors.database.RemoveDatabaseInteractor
import com.infinum.dbinspector.data.interactors.database.RenameDatabaseInteractor
import com.infinum.dbinspector.data.interactors.history.ClearHistoryInteractor
import com.infinum.dbinspector.data.interactors.history.GetExecutionInteractor
import com.infinum.dbinspector.data.interactors.history.GetHistoryInteractor
import com.infinum.dbinspector.data.interactors.history.RemoveExecutionInteractor
import com.infinum.dbinspector.data.interactors.history.SaveExecutionInteractor
import com.infinum.dbinspector.data.interactors.pragma.GetForeignKeysInteractor
import com.infinum.dbinspector.data.interactors.pragma.GetIndexesInteractor
import com.infinum.dbinspector.data.interactors.pragma.GetTableInfoInteractor
import com.infinum.dbinspector.data.interactors.pragma.GetUserVersionInteractor
import com.infinum.dbinspector.data.interactors.raw.GetAffectedRowsInteractor
import com.infinum.dbinspector.data.interactors.raw.GetRawQueryHeadersInteractor
import com.infinum.dbinspector.data.interactors.raw.GetRawQueryInteractor
import com.infinum.dbinspector.data.interactors.schema.table.DropTableContentByNameInteractor
import com.infinum.dbinspector.data.interactors.schema.table.GetTableByNameInteractor
import com.infinum.dbinspector.data.interactors.schema.table.GetTablesInteractor
import com.infinum.dbinspector.data.interactors.schema.trigger.DropTriggerByNameInteractor
import com.infinum.dbinspector.data.interactors.schema.trigger.GetTriggerByNameInteractor
import com.infinum.dbinspector.data.interactors.schema.trigger.GetTriggersInteractor
import com.infinum.dbinspector.data.interactors.schema.view.DropViewByNameInteractor
import com.infinum.dbinspector.data.interactors.schema.view.GetViewByNameInteractor
import com.infinum.dbinspector.data.interactors.schema.view.GetViewsInteractor
import com.infinum.dbinspector.data.interactors.settings.GetSettingsInteractor
import com.infinum.dbinspector.data.interactors.settings.RemoveIgnoredTableNameInteractor
import com.infinum.dbinspector.data.interactors.settings.SaveBlobPreviewModeInteractor
import com.infinum.dbinspector.data.interactors.settings.SaveIgnoredTableNameInteractor
import com.infinum.dbinspector.data.interactors.settings.SaveLinesCountInteractor
import com.infinum.dbinspector.data.interactors.settings.SaveLinesLimitInteractor
import com.infinum.dbinspector.data.interactors.settings.SaveTruncateModeInteractor
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

public object Data {

    public object Constants {

        public object Limits {
            public const val PAGE_SIZE: Int = 100
            public const val INITIAL_PAGE: Int = 1
        }

        public object Settings {
            public const val LINES_LIMIT_MAXIMUM: Int = 100
        }

        public object Name {
            public const val PROTO_FILENAME_SETTINGS: String = "settings-entity.pb"
            public const val PROTO_FILENAME_HISTORY: String = "history-entity.pb"
        }
    }

    public object Qualifiers {

        public object Name {
            public val DATASTORE_SETTINGS: StringQualifier = StringQualifier("data.qualifiers.name.datastore.settings")
            public val DATASTORE_HISTORY: StringQualifier = StringQualifier("data.qualifiers.name.datastore.history")
        }

        public object Schema {
            public val TABLES: StringQualifier = StringQualifier("data.qualifiers.tables")
            public val TABLE_BY_NAME: StringQualifier = StringQualifier("data.qualifiers.table_by_name")
            public val DROP_TABLE_CONTENT: StringQualifier = StringQualifier("data.qualifiers.drop_table_content")

            public val VIEWS: StringQualifier = StringQualifier("data.qualifiers.views")
            public val VIEW_BY_NAME: StringQualifier = StringQualifier("data.qualifiers.view_by_name")
            public val DROP_VIEW: StringQualifier = StringQualifier("data.qualifiers.drop_view")

            public val TRIGGERS: StringQualifier = StringQualifier("data.qualifiers.triggers")
            public val TRIGGER_BY_NAME: StringQualifier = StringQualifier("data.qualifiers.trigger_by_name")
            public val DROP_TRIGGER: StringQualifier = StringQualifier("data.qualifiers.drop_trigger")

            public val RAW_QUERY: StringQualifier = StringQualifier("data.qualifiers.raw_query")
        }

        public object Pragma {
            public val TABLE_INFO: StringQualifier = StringQualifier("data.qualifiers.table_info")
            public val FOREIGN_KEYS: StringQualifier = StringQualifier("data.qualifiers.foreign_keys")
            public val INDEXES: StringQualifier = StringQualifier("data.qualifiers.indexes")
        }
    }

    public fun modules(): List<Module> =
        listOf(
            raw(),
            memory(),
            local(),
            database(),
            connection(),
            settings(),
            schema(),
            pragma(),
            rawQuery(),
            history()
        )

    private fun raw() = module {
        single<Sources.Raw> { AndroidDatabasesSource() }
    }

    private fun memory() = module {
        factory<Paginator>(qualifier = Qualifiers.Schema.TABLES) { CursorPaginator() }
        factory<Paginator>(qualifier = Qualifiers.Schema.TABLE_BY_NAME) { CursorPaginator() }
        factory<Paginator>(qualifier = Qualifiers.Schema.DROP_TABLE_CONTENT) { CursorPaginator() }

        factory<Paginator>(qualifier = Qualifiers.Schema.VIEWS) { CursorPaginator() }
        factory<Paginator>(qualifier = Qualifiers.Schema.VIEW_BY_NAME) { CursorPaginator() }
        factory<Paginator>(qualifier = Qualifiers.Schema.DROP_VIEW) { CursorPaginator() }

        factory<Paginator>(qualifier = Qualifiers.Schema.TRIGGERS) { CursorPaginator() }
        factory<Paginator>(qualifier = Qualifiers.Schema.TRIGGER_BY_NAME) { CursorPaginator() }
        factory<Paginator>(qualifier = Qualifiers.Schema.DROP_TRIGGER) { CursorPaginator() }

        factory<Paginator>(qualifier = Qualifiers.Pragma.TABLE_INFO) { CursorPaginator() }
        factory<Paginator>(qualifier = Qualifiers.Pragma.FOREIGN_KEYS) { CursorPaginator() }
        factory<Paginator>(qualifier = Qualifiers.Pragma.INDEXES) { CursorPaginator() }

        factory<Paginator>(qualifier = Qualifiers.Schema.RAW_QUERY) { CursorPaginator() }

        single<Sources.Memory.Connection> { AndroidConnectionSource() }

        single<Sources.Memory.Distance> { LevenshteinDistance() }
    }

    private fun local() = module {
        single(qualifier = Qualifiers.Name.DATASTORE_SETTINGS) { PROTO_FILENAME_SETTINGS }
        single(qualifier = Qualifiers.Name.DATASTORE_HISTORY) { PROTO_FILENAME_HISTORY }

        single<Serializer<SettingsEntity>>(qualifier = Qualifiers.Name.DATASTORE_SETTINGS) { SettingsSerializer() }
        single<Serializer<HistoryEntity>>(qualifier = Qualifiers.Name.DATASTORE_HISTORY) { HistorySerializer() }

        single<Sources.Local.Settings> {
            val context: Context = get()
            SettingsDataStore(
                DataStoreFactory.create(
                    get(qualifier = Qualifiers.Name.DATASTORE_SETTINGS)
                ) {
                    context.dataStoreFile(get(qualifier = Qualifiers.Name.DATASTORE_SETTINGS))
                }
            )
        }
        single<Sources.Local.History> {
            val context: Context = get()

            HistoryDataStore(
                DataStoreFactory.create(
                    get(qualifier = Qualifiers.Name.DATASTORE_HISTORY)
                ) {
                    context.dataStoreFile(get(qualifier = Qualifiers.Name.DATASTORE_HISTORY))
                }
            )
        }

        factory<Sources.Local.Schema> {
            SchemaSource(
                get(qualifier = Qualifiers.Schema.TABLES),
                get(qualifier = Qualifiers.Schema.TABLE_BY_NAME),
                get(qualifier = Qualifiers.Schema.DROP_TABLE_CONTENT),
                get(qualifier = Qualifiers.Schema.VIEWS),
                get(qualifier = Qualifiers.Schema.VIEW_BY_NAME),
                get(qualifier = Qualifiers.Schema.DROP_VIEW),
                get(qualifier = Qualifiers.Schema.TRIGGERS),
                get(qualifier = Qualifiers.Schema.TRIGGER_BY_NAME),
                get(qualifier = Qualifiers.Schema.DROP_TRIGGER),
                get()
            )
        }

        factory<Sources.Local.Pragma> {
            PragmaSource(
                get(qualifier = Qualifiers.Pragma.TABLE_INFO),
                get(qualifier = Qualifiers.Pragma.FOREIGN_KEYS),
                get(qualifier = Qualifiers.Pragma.INDEXES),
                get()
            )
        }

        factory<Sources.Local.RawQuery> {
            RawQuerySource(
                get(qualifier = Qualifiers.Schema.RAW_QUERY),
                get()
            )
        }
    }
    private fun database() = module {
        factory<Interactors.GetDatabases> { GetDatabasesInteractor(get()) }
        factory<Interactors.ImportDatabases> { ImportDatabasesInteractor(get()) }
        factory<Interactors.RemoveDatabase> { RemoveDatabaseInteractor(get()) }
        factory<Interactors.RenameDatabase> { RenameDatabaseInteractor(get()) }
        factory<Interactors.CopyDatabase> { CopyDatabaseInteractor(get()) }
    }

    private fun connection() = module {
        single<Interactors.OpenConnection> { OpenConnectionInteractor(get()) }
        single<Interactors.CloseConnection> { CloseConnectionInteractor(get()) }
    }

    private fun settings() = module {
        factory<Interactors.GetSettings> { GetSettingsInteractor(get()) }
        factory<Interactors.SaveLinesLimit> { SaveLinesLimitInteractor(get()) }
        factory<Interactors.SaveLinesCount> { SaveLinesCountInteractor(get()) }
        factory<Interactors.SaveTruncateMode> { SaveTruncateModeInteractor(get()) }
        factory<Interactors.SaveBlobPreviewMode> { SaveBlobPreviewModeInteractor(get()) }
        factory<Interactors.SaveIgnoredTableName> { SaveIgnoredTableNameInteractor(get()) }
        factory<Interactors.RemoveIgnoredTableName> { RemoveIgnoredTableNameInteractor(get()) }
    }

    private fun schema() = module {
        factory<Interactors.GetTables> { GetTablesInteractor(get()) }
        factory<Interactors.GetTableByName> { GetTableByNameInteractor(get()) }
        factory<Interactors.DropTableContentByName> { DropTableContentByNameInteractor(get()) }
        factory<Interactors.GetViews> { GetViewsInteractor(get()) }
        factory<Interactors.GetViewByName> { GetViewByNameInteractor(get()) }
        factory<Interactors.DropViewByName> { DropViewByNameInteractor(get()) }
        factory<Interactors.GetTriggers> { GetTriggersInteractor(get()) }
        factory<Interactors.GetTriggerByName> { GetTriggerByNameInteractor(get()) }
        factory<Interactors.DropTriggerByName> { DropTriggerByNameInteractor(get()) }
    }

    private fun pragma() = module {
        factory<Interactors.GetUserVersion> { GetUserVersionInteractor(get()) }
        factory<Interactors.GetTableInfo> { GetTableInfoInteractor(get()) }
        factory<Interactors.GetForeignKeys> { GetForeignKeysInteractor(get()) }
        factory<Interactors.GetIndexes> { GetIndexesInteractor(get()) }
    }

    private fun rawQuery() = module {
        factory<Interactors.GetRawQueryHeaders> { GetRawQueryHeadersInteractor(get()) }
        factory<Interactors.GetRawQuery> { GetRawQueryInteractor(get()) }
        factory<Interactors.GetAffectedRows> { GetAffectedRowsInteractor(get()) }
    }

    private fun history() = module {
        factory<Interactors.GetHistory> { GetHistoryInteractor(get()) }
        factory<Interactors.SaveExecution> { SaveExecutionInteractor(get()) }
        factory<Interactors.ClearHistory> { ClearHistoryInteractor(get()) }
        factory<Interactors.RemoveExecution> { RemoveExecutionInteractor(get()) }
        factory<Interactors.GetExecution> { GetExecutionInteractor(get(), get()) }
    }
}
