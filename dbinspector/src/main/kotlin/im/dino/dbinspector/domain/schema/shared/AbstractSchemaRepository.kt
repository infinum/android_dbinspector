package im.dino.dbinspector.domain.schema.shared

import im.dino.dbinspector.data.models.local.QueryResult
import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.shared.base.BaseInteractor
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query

internal abstract class AbstractSchemaRepository(
    private val getPageInteractor: BaseInteractor<Query, QueryResult>,
    private val getByNameInteractor: BaseInteractor<Query, QueryResult>,
    private val dropByNameInteractor: BaseInteractor<Query, QueryResult>
) : Repositories.Schema {

    override suspend fun getPage(query: Query): Page =
        getPageInteractor(query).let {
            Page(
                fields = it.rows.map { row -> row.fields.toList() }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun getByName(query: Query): Page =
        getByNameInteractor(query).let {
            Page(
                fields = it.rows.map { row -> row.fields.toList() }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun dropByName(query: Query): Page =
        dropByNameInteractor(query).let {
            Page(
                fields = it.rows.map { row -> row.fields.toList() }.flatten(),
                nextPage = it.nextPage
            )
        }
}
