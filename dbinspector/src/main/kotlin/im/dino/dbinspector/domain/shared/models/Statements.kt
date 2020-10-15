package im.dino.dbinspector.domain.shared.models

import im.dino.dbinspector.domain.shared.models.dsl.delete
import im.dino.dbinspector.domain.shared.models.dsl.dropTrigger
import im.dino.dbinspector.domain.shared.models.dsl.dropView
import im.dino.dbinspector.domain.shared.models.dsl.select

object Statements {

    object Pragma {

        @Suppress("FunctionOnlyReturningConstant")
        fun userVersion() = "PRAGMA user_version"

        fun tableInfo(name: String) =
            String.format(
                "PRAGMA table_info(\"%s\")",
                name
            )

        fun foreignKeys(name: String) =
            String.format(
                "PRAGMA foreign_key_list(\"%s\")",
                name
            )

        fun indexes(name: String) =
            String.format(
                "PRAGMA index_list(\"%s\")",
                name
            )
    }

    object Schema {

        fun tables(query: String? = null, direction: Direction = Direction.ASCENDING) =
            select {
                columns("name")
                from("sqlite_master")
                where {
                    "type" eq "table"
                    query?.let { "name" like it }
                }
                orderBy(direction, "name")
            }

        fun views(query: String? = null, direction: Direction = Direction.ASCENDING) =
            select {
                columns("name")
                from("sqlite_master")
                where {
                    "type" eq "view"
                    query?.let { "name" like it }
                }
                orderBy(direction, "name")
            }

        fun triggers(query: String? = null, direction: Direction = Direction.ASCENDING) =
            select {
                columns("name")
                from("sqlite_master")
                where {
                    "type" eq "trigger"
                    query?.let { "name" like it }
                }
                orderBy(direction, "name")
            }

        fun table(name: String) =
            select {
                columns()
                from(name)
            }

        fun view(name: String) =
            select {
                columns()
                from(name)
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
}
