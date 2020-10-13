package im.dino.dbinspector.ui.schema.tables

import androidx.paging.PagingData
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.shared.models.Statements
import im.dino.dbinspector.ui.schema.shared.SchemaSourceViewModel

internal class TablesViewModel(
    private val getSchema: UseCases.GetTables
) : SchemaSourceViewModel() {

    override fun schemaStatement(): String =
        Statements.Schema.tables()

    override fun dataSource(databasePath: String, statement: String) = TablesDataSource(databasePath, statement, getSchema)

    override fun observe(action: suspend () -> Unit) = Unit

    override fun query(
        databasePath: String,
        onData: suspend (value: PagingData<String>) -> Unit
    ) {
        launch {
            pageFlow(databasePath, schemaStatement()) {
                onData(it)
            }
        }
    }
}
