package com.infinum.dbinspector.domain.schema.shared

import com.infinum.dbinspector.data.models.local.QueryResult
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.base.BaseInteractor
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Query

internal abstract class AbstractSchemaRepository(
    private val getPageInteractor: BaseInteractor<Query, QueryResult>,
    private val getByNameInteractor: BaseInteractor<Query, QueryResult>,
    private val dropByNameInteractor: BaseInteractor<Query, QueryResult>
) : Repositories.Schema {

    override suspend fun getPage(input: Query): Page =
        getPageInteractor(input).let {
            Page(
                beforeCount = it.beforeCount,
                afterCount = it.afterCount,
                fields = it.rows.map { row -> row.fields.toList() }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun getByName(query: Query): Page =
        getByNameInteractor(query).let {
            Page(
                beforeCount = it.beforeCount,
                afterCount = it.afterCount,
                fields = it.rows.map { row -> row.fields.toList() }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun dropByName(query: Query): Page =
        dropByNameInteractor(query).let {
            Page(
                beforeCount = it.beforeCount,
                afterCount = it.afterCount,
                fields = it.rows.map { row -> row.fields.toList() }.flatten(),
                nextPage = it.nextPage
            )
        }
}
