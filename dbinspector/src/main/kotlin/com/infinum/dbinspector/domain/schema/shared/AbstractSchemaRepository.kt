package com.infinum.dbinspector.domain.schema.shared

import com.infinum.dbinspector.data.models.local.cursor.BlobPreviewType
import com.infinum.dbinspector.data.models.local.cursor.QueryResult
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.base.BaseInteractor
import com.infinum.dbinspector.domain.shared.models.Direction
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Query
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.ui.shared.Constants

internal abstract class AbstractSchemaRepository(
    private val getPageInteractor: BaseInteractor<Query, QueryResult>,
    private val getByNameInteractor: BaseInteractor<Query, QueryResult>,
    private val dropByNameInteractor: BaseInteractor<Query, QueryResult>,
    private val mapper: Mappers.SchemaCell
) : Repositories.Schema {

    override suspend fun getPage(input: ContentParameters): Page =
        getPageInteractor(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = input.order,
                pageSize = input.pageSize,
                page = input.page,
                blobPreviewType = input.blobPreviewType
            )
        ).let {
            Page(
                beforeCount = it.beforeCount,
                afterCount = it.afterCount,
                fields = it.rows.map { row ->
                    row.fields.toList().map { field -> mapper.toDomain(field) }
                }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun getByName(input: ContentParameters): Page =
        getByNameInteractor(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = input.order,
                pageSize = input.pageSize,
                page = input.page,
                blobPreviewType = input.blobPreviewType
            )
        ).let {
            Page(
                beforeCount = it.beforeCount,
                afterCount = it.afterCount,
                fields = it.rows.map { row ->
                    row.fields.toList().map { field -> mapper.toDomain(field) }
                }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun dropByName(input: ContentParameters): Page =
        dropByNameInteractor(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = input.order,
                pageSize = input.pageSize,
                page = input.page,
                blobPreviewType = input.blobPreviewType
            )
        ).let {
            Page(
                beforeCount = it.beforeCount,
                afterCount = it.afterCount,
                fields = it.rows.map { row ->
                    row.fields.toList().map { field -> mapper.toDomain(field) }
                }.flatten(),
                nextPage = it.nextPage
            )
        }
}
