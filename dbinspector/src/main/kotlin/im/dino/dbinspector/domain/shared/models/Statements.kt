package im.dino.dbinspector.domain.shared.models

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

        fun tables(query: String? = null, order: Sort = Sort.ASCENDING) =
            String.format(
                "SELECT name FROM sqlite_master WHERE type = 'table' ORDER BY name %s",
                order.rawValue
            )

//        fun searchTables(query: String, order: Sort = Sort.ASCENDING) =
//            String.format(
//                "SELECT name FROM sqlite_master WHERE type='table' AND name LIKE \"%%%s%%\" ORDER BY name %s",
//                query,
//                order.rawValue
//            )

        fun views(query: String? = null, order: Sort = Sort.ASCENDING) =
            String.format(
                "SELECT name FROM sqlite_master WHERE type = 'view' ORDER BY name %s",
                order.rawValue
            )

//        fun searchViews(query: String, order: Sort = Sort.ASCENDING) =
//            String.format(
//                "SELECT name FROM sqlite_master WHERE type = 'view' AND name LIKE \"%%%s%%\" ORDER BY name %s",
//                query,
//                order.rawValue
//            )

        fun triggers(query: String? = null, order: Sort = Sort.ASCENDING) =
            String.format(
                "SELECT name FROM sqlite_master WHERE type='trigger' ORDER BY name %s",
                order.rawValue
            )

//        fun searchTriggers(query: String, order: Sort = Sort.ASCENDING) =
//            String.format(
//                "SELECT name FROM sqlite_master WHERE type = 'trigger' AND name LIKE \"%%%s%%\" ORDER BY name %s",
//                query,
//                order.rawValue
//            )

        fun table(name: String) =
            String.format(
                "SELECT * FROM \"%s\"",
                name
            )

        fun view(name: String) =
            String.format(
                "SELECT * FROM \"%s\"",
                name
            )

        fun trigger(name: String) =
            String.format(
                "SELECT name, sql FROM sqlite_master WHERE type = 'trigger' AND name = \"%s\" LIMIT 1",
                name
            )

        fun dropTableContent(name: String) =
            String.format(
                "DELETE FROM \"%s\"",
                name
            )

        fun dropView(name: String) =
            String.format(
                "DROP VIEW \"%s\"",
                name
            )

        fun dropTrigger(name: String) =
            String.format(
                "DROP TRIGGER \"%s\"",
                name
            )
    }
}
