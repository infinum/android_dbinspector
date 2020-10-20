package im.dino.dbinspector.ui.schema.triggers

import androidx.paging.PagingData
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.schema.shared.SchemaSourceViewModel

internal class TriggersViewModel(
    private val getSchema: UseCases.GetTriggers
) : SchemaSourceViewModel() {

    override fun schemaStatement(query: String?): String =
        Statements.Schema.triggers(query = query)

    override fun dataSource(databasePath: String, statement: String) =
        TriggersDataSource(
            databasePath = databasePath,
            statement = statement,
            getSchema = getSchema
        )

    override fun query(
        databasePath: String,
        query: String?,
        onData: suspend (value: PagingData<String>) -> Unit
    ) {
        launch {
            pageFlow(databasePath, schemaStatement(query)) {
                onData(it)
            }
        }
    }
}
