package com.infinum.dbinspector.domain.schema.shared

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.base.BaseInteractor
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters

internal abstract class AbstractSchemaRepository(
    private val getPageInteractor: BaseInteractor<Query, QueryResult>,
    private val getByNameInteractor: BaseInteractor<Query, QueryResult>,
    private val dropByNameInteractor: BaseInteractor<Query, QueryResult>,
    private val mapper: Mappers.Page,
    private val converter: Converters.Schema
) : Repositories.Schema {

    override suspend fun getPage(input: ContentParameters): Page =
        mapper(getPageInteractor(converter getPage input))

    override suspend fun getByName(input: ContentParameters): Page =
        mapper(getByNameInteractor(converter getByName input))

    override suspend fun dropByName(input: ContentParameters): Page =
        mapper(dropByNameInteractor(converter dropByName input))
}
