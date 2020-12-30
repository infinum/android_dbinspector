package com.infinum.dbinspector.domain.schema.shared

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.base.BaseInteractor
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters

internal abstract class AbstractSchemaRepository(
    private val getPageInteractor: BaseInteractor<Query, QueryResult>,
    private val getByNameInteractor: BaseInteractor<Query, QueryResult>,
    private val dropByNameInteractor: BaseInteractor<Query, QueryResult>,
    private val mapper: Mappers.Page
) : Repositories.Schema {

    override suspend fun getPage(input: ContentParameters): Page =
        getPageInteractor(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = mapper.sortMapper().mapDomainToLocal(input.sort),
                pageSize = input.pageSize,
                page = input.page,
                blobPreview = mapper.blobPreviewModeMapper().mapDomainToLocal(input.blobPreviewMode)
            )
        ).let { mapper.mapLocalToDomain(it) }

    override suspend fun getByName(input: ContentParameters): Page =
        getByNameInteractor(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = mapper.sortMapper().mapDomainToLocal(input.sort),
                pageSize = input.pageSize,
                page = input.page,
                blobPreview = mapper.blobPreviewModeMapper().mapDomainToLocal(input.blobPreviewMode)
            )
        ).let { mapper.mapLocalToDomain(it) }

    override suspend fun dropByName(input: ContentParameters): Page =
        dropByNameInteractor(
            Query(
                databasePath = input.databasePath,
                database = input.database,
                statement = input.statement,
                order = mapper.sortMapper().mapDomainToLocal(input.sort),
                pageSize = input.pageSize,
                page = input.page,
                blobPreview = mapper.blobPreviewModeMapper().mapDomainToLocal(input.blobPreviewMode)
            )
        ).let { mapper.mapLocalToDomain(it) }
}
