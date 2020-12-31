package com.infinum.dbinspector.domain.schema.shared

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.base.BaseInteractor
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters

internal abstract class AbstractSchemaRepository(
    private val getPageInteractor: BaseInteractor<Query, QueryResult>,
    private val getByNameInteractor: BaseInteractor<Query, QueryResult>,
    private val dropByNameInteractor: BaseInteractor<Query, QueryResult>,
    private val control: Control.Schema
) : Repositories.Schema {

    override suspend fun getPage(input: ContentParameters): Page =
        control.mapper(getPageInteractor(control.converter getPage input))

    override suspend fun getByName(input: ContentParameters): Page =
        control.mapper(getByNameInteractor(control.converter getByName input))

    override suspend fun dropByName(input: ContentParameters): Page =
        control.mapper(dropByNameInteractor(control.converter dropByName input))
}
