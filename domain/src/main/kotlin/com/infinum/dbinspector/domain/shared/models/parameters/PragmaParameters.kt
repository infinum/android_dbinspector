package com.infinum.dbinspector.domain.shared.models.parameters

import com.infinum.dbinspector.domain.Domain
import com.infinum.dbinspector.domain.connection.models.DatabaseConnection
import com.infinum.dbinspector.domain.shared.base.PageParameters

public sealed class PragmaParameters(override var page: Int?) : PageParameters(page) {

    public data class Version(
        val databasePath: String,
        val connection: DatabaseConnection? = null,
        val statement: String,
        override var page: Int? = null
    ) : PragmaParameters(page)

    public data class Pragma(
        val databasePath: String,
        val connection: DatabaseConnection? = null,
        val statement: String,
        val sort: SortParameters = SortParameters(),
        override var page: Int? = Domain.Constants.Limits.INITIAL_PAGE,
        val pageSize: Int = Domain.Constants.Limits.PAGE_SIZE
    ) : PragmaParameters(page)
}
