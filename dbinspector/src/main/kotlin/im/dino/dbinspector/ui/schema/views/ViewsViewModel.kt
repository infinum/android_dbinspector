package im.dino.dbinspector.ui.schema.views

import androidx.paging.PagingData
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.schema.shared.SchemaSourceViewModel

internal class ViewsViewModel(
    private val getSchema: UseCases.GetViews
) : SchemaSourceViewModel() {

    override fun schemaStatement(query: String?): String =
        Statements.Schema.views(query = query)

    override fun dataSource(databasePath: String, statement: String) =
        ViewsDataSource(
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
