package com.infinum.dbinspector.data

import com.infinum.dbinspector.data.source.local.PragmaSource
import com.infinum.dbinspector.data.source.local.SchemaSource
import com.infinum.dbinspector.data.source.memory.connection.AndroidConnectionSource
import com.infinum.dbinspector.data.source.memory.pagination.CursorPaginator
import com.infinum.dbinspector.data.source.memory.pagination.Paginator
import com.infinum.dbinspector.data.source.raw.AndroidDatabasesSource
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

object Data {

    object Qualifiers {

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

        single<Sources.Memory> { AndroidConnectionSource() }
    }

    private fun local() = module {
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
                get(qualifier = Qualifiers.Schema.DROP_TRIGGER)
            )
        }

        factory<Sources.Local.Pragma> {
            PragmaSource(
                get(qualifier = Qualifiers.Pragma.TABLE_INFO),
                get(qualifier = Qualifiers.Pragma.FOREIGN_KEYS),
                get(qualifier = Qualifiers.Pragma.INDEXES)
            )
        }
    }
}
