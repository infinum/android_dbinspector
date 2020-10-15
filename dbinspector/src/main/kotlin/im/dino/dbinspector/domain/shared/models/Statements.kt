package im.dino.dbinspector.domain.shared.models

import im.dino.dbinspector.domain.shared.models.specs.statementSpec

object Statements {

    private const val ASTERISK = "*"

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

        fun tables(query: String? = null, order: Order = Order.ASCENDING) =
            statementSpec {
                command("SELECT")
                columns(listOf("name"))
                table("sqlite_master")
                type("table")
                query(query)
                order(order)
            }

        fun views(query: String? = null, order: Order = Order.ASCENDING) =
            statementSpec {
                command("SELECT")
                columns(listOf("name"))
                table("sqlite_master")
                type("view")
                query(query)
                order(order)
            }

        fun triggers(query: String? = null, order: Order = Order.ASCENDING) =
            statementSpec {
                command("SELECT")
                columns(listOf("name"))
                table("sqlite_master")
                type("trigger")
                query(query)
                order(order)
            }

        fun table(name: String) =
            statementSpec {
                command("SELECT")
                columns(listOf(ASTERISK))
                table(name)
            }

        fun view(name: String) =
            statementSpec {
                command("SELECT")
                columns(listOf(ASTERISK))
                table(name)
            }

        fun trigger(name: String) =
            statementSpec {
                command("SELECT")
                columns(listOf("name", "sql"))
                table("sqlite_master")
                type("trigger")
                equalsTo(name)
                limit(1)
            }

        fun dropTableContent(name: String) =
            statementSpec {
                command("DELETE FROM")
                table(name)
            }

        fun dropView(name: String) =
            statementSpec {
                command("DROP VIEW")
                table(name)
            }

        fun dropTrigger(name: String) =
            statementSpec {
                command("DROP TRIGGER")
                table(name)
            }
    }
}
