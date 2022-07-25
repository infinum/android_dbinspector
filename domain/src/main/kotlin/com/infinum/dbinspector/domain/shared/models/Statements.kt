package com.infinum.dbinspector.domain.shared.models

import com.infinum.dbinspector.domain.shared.models.dsl.changes
import com.infinum.dbinspector.domain.shared.models.dsl.delete
import com.infinum.dbinspector.domain.shared.models.dsl.dropTrigger
import com.infinum.dbinspector.domain.shared.models.dsl.dropView
import com.infinum.dbinspector.domain.shared.models.dsl.pragma
import com.infinum.dbinspector.domain.shared.models.dsl.select

public object Statements {

    public object Pragma {

        public fun userVersion(): String =
            pragma {
                name("user_version")
            }

        public fun tableInfo(name: String): String =
            pragma {
                name("table_info")
                value(name)
            }

        public fun foreignKeys(name: String): String =
            pragma {
                name("foreign_key_list")
                value(name)
            }

        public fun indexes(name: String): String =
            pragma {
                name("index_list")
                value(name)
            }
    }

    public object Schema {

        public fun tables(query: String? = null, sort: Sort = Sort.ASCENDING): String =
            select {
                columns("name")
                from("sqlite_master")
                where {
                    "type" eq "table"
                    query?.let { "name" like it }
                }
                orderBy(sort, "name")
            }

        public fun views(query: String? = null, sort: Sort = Sort.ASCENDING): String =
            select {
                columns("name")
                from("sqlite_master")
                where {
                    "type" eq "view"
                    query?.let { "name" like it }
                }
                orderBy(sort, "name")
            }

        public fun triggers(query: String? = null, sort: Sort = Sort.ASCENDING): String =
            select {
                columns("name")
                from("sqlite_master")
                where {
                    "type" eq "trigger"
                    query?.let { "name" like it }
                }
                orderBy(sort, "name")
            }

        public fun table(name: String, orderBy: String? = null, sort: Sort = Sort.ASCENDING): String =
            select {
                columns()
                from(name)
                orderBy(sort, orderBy)
            }

        public fun view(name: String): String =
            select {
                columns()
                from(name)
            }

        public fun trigger(name: String): String =
            select {
                columns("name", "sql")
                from("sqlite_master")
                where {
                    "type" eq "trigger"
                    "name" eq name
                }
                limit(1)
            }

        public fun dropTableContent(name: String): String =
            delete {
                from(name)
            }

        public fun dropView(name: String): String =
            dropView {
                name(name)
            }

        public fun dropTrigger(name: String): String =
            dropTrigger {
                name(name)
            }
    }

    public object RawQuery {

        public fun affectedRows(): String = changes()
    }
}
