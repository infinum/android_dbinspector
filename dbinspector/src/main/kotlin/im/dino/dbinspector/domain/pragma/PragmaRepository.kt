package im.dino.dbinspector.domain.pragma

import im.dino.dbinspector.domain.Interactors
import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.pragma.models.TriggerInfoColumns
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query
import java.util.Locale

internal class PragmaRepository(
    private val userVersion: Interactors.GetUserVersion,
    private val tableInfo: Interactors.GetTableInfo,
    private val foreignKeys: Interactors.GetForeignKeys,
    private val indexes: Interactors.GetIndexes
) : Repositories.Pragma {

    override suspend fun getUserVersion(query: Query): Page =
        userVersion(query).let {
            Page(
                fields = it.rows.map { row -> row.fields.toList() }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun getTableInfo(query: Query): Page =
        tableInfo(query).let {
            Page(
                fields = it.rows.map { row -> row.fields.toList() }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun getTriggerInfo(query: Query): Page =
        Page(
            fields = TriggerInfoColumns.values().map { it.name.toLowerCase(Locale.getDefault()) },
        )

    override suspend fun getForeignKeys(query: Query): Page =
        foreignKeys(query).let {
            Page(
                fields = it.rows.map { row -> row.fields.toList() }.flatten(),
                nextPage = it.nextPage
            )
        }

    override suspend fun getIndexes(query: Query): Page =
        indexes(query).let {
            Page(
                fields = it.rows.map { row -> row.fields.toList() }.flatten(),
                nextPage = it.nextPage
            )
        }
}
