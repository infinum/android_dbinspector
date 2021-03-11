package com.infinum.dbinspector.data

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import com.infinum.dbinspector.data.Data.Constants.Name.PROTO_FILENAME_HISTORY
import com.infinum.dbinspector.data.Data.Constants.Name.PROTO_FILENAME_SETTINGS
import com.infinum.dbinspector.data.models.local.proto.output.HistoryEntity
import com.infinum.dbinspector.data.models.local.proto.output.SettingsEntity
import com.infinum.dbinspector.data.source.local.cursor.PragmaSource
import com.infinum.dbinspector.data.source.local.cursor.RawQuerySource
import com.infinum.dbinspector.data.source.local.cursor.SchemaSource
import com.infinum.dbinspector.data.source.local.proto.history.HistoryDataStore
import com.infinum.dbinspector.data.source.local.proto.history.HistorySerializer
import com.infinum.dbinspector.data.source.local.proto.settings.SettingsDataStore
import com.infinum.dbinspector.data.source.local.proto.settings.SettingsSerializer
import com.infinum.dbinspector.data.source.memory.connection.AndroidConnectionSource
import com.infinum.dbinspector.data.source.memory.pagination.CursorPaginator
import com.infinum.dbinspector.data.source.memory.pagination.Paginator
import com.infinum.dbinspector.data.source.raw.AndroidDatabasesSource
import com.infinum.dbinspector.extensions.dataStoreFile
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

internal object Data {

    object Constants {

        object Limits {
            const val PAGE_SIZE = 100
            const val INITIAL_PAGE = 1
        }

        object Settings {
            const val LINES_LIMIT_MAXIMUM = 100
        }

        object Name {
            const val PROTO_FILENAME_SETTINGS = "settings-entity.pb"
            const val PROTO_FILENAME_HISTORY = "history-entity.pb"
        }
    }

    object Qualifiers {

        object Name {
            val DATASTORE_SETTINGS = StringQualifier("data.qualifiers.name.datastore.settings")
            val DATASTORE_HISTORY = StringQualifier("data.qualifiers.name.datastore.history")
        }

        object Schema {
            val TABLES = StringQualifier("data.qualifiers.tables")
            val TABLE_BY_NAME = StringQualifier("data.qualifiers.table_by_name")
            val DROP_TABLE_CONTENT = StringQualifier("data.qualifiers.drop_table_content")

            val VIEWS = StringQualifier("data.qualifiers.views")
            val VIEW_BY_NAME = StringQualifier("data.qualifiers.view_by_name")
            val DROP_VIEW = StringQualifier("data.qualifiers.drop_view")

            val TRIGGERS = StringQualifier("data.qualifiers.triggers")
            val TRIGGER_BY_NAME = StringQualifier("data.qualifiers.trigger_by_name")
            val DROP_TRIGGER = StringQualifier("data.qualifiers.drop_trigger")

            val RAW_QUERY = StringQualifier("data.qualifiers.raw_query")
        }

        object Pragma {
            val TABLE_INFO = StringQualifier("data.qualifiers.table_info")
            val FOREIGN_KEYS = StringQualifier("data.qualifiers.foreign_keys")
            val INDEXES = StringQualifier("data.qualifiers.indexes")
        }
    }

    fun modules(): List<Module> =
        listOf(
            raw(),
            memory(),
            local()
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

        single<Sources.Memory> { AndroidConnectionSource() }
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
}
