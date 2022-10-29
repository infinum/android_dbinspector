package com.infinum.dbinspector.domain.shared.models

import com.infinum.dbinspector.domain.shared.models.dsl.changes
import com.infinum.dbinspector.domain.shared.models.dsl.delete
import com.infinum.dbinspector.domain.shared.models.dsl.dropTrigger
import com.infinum.dbinspector.domain.shared.models.dsl.dropView
import com.infinum.dbinspector.domain.shared.models.dsl.pragma
import com.infinum.dbinspector.domain.shared.models.dsl.select

internal object Statements {

    object Pragma {

        fun userVersion() =
            pragma {
                name("user_version")
            }

        fun tableInfo(name: String) =
            pragma {
                name("table_info")
                value(name)
            }

        fun foreignKeys(name: String) =
            pragma {
                name("foreign_key_list")
                value(name)
            }

        fun indexes(name: String) =
            pragma {
                name("index_list")
                value(name)
            }
    }

    object Schema {

        fun tables(query: String? = null, sort: Sort = Sort.ASCENDING) =
            select {
                columns("name")
                from("sqlite_master")
                where {
                    "type" eq "table"
                    query?.let { "name" like it }
                }
                orderBy(sort, "name")
            }

        fun views(query: String? = null, sort: Sort = Sort.ASCENDING) =
            select {
                columns("name")
                from("sqlite_master")
                where {
                    "type" eq "view"
                    query?.let { "name" like it }
                }
                orderBy(sort, "name")
            }

        fun triggers(query: String? = null, sort: Sort = Sort.ASCENDING) =
            select {
                columns("name")
                from("sqlite_master")
                where {
                    "type" eq "trigger"
                    query?.let { "name" like it }
                }
                orderBy(sort, "name")
            }

        fun table(name: String, orderBy: String? = null, sort: Sort = Sort.ASCENDING) =
            select {
                columns()
                from(name)
                orderBy(sort, orderBy)
            }

        fun view(name: String, orderBy: String? = null, sort: Sort = Sort.ASCENDING) =
            select {
                columns()
                from(name)
                orderBy(sort, orderBy)
            }

        fun trigger(name: String) =
            select {
                columns("name", "sql")
                from("sqlite_master")
                where {
                    "type" eq "trigger"
                    "name" eq name
                }
                limit(1)
            }

        fun dropTableContent(name: String) =
            delete {
                from(name)
            }

        fun dropView(name: String) =
            dropView {
                name(name)
            }

        fun dropTrigger(name: String) =
            dropTrigger {
                name(name)
            }
    }

    object RawQuery {

        fun affectedRows() = changes()
    }
}
