package im.dino.dbinspector.domain.pragma.usecases

import im.dino.dbinspector.domain.Repositories
import im.dino.dbinspector.domain.UseCases
import im.dino.dbinspector.domain.pragma.models.TableInfoColumns
import im.dino.dbinspector.domain.shared.models.Page
import im.dino.dbinspector.domain.shared.models.Query

internal class GetTableInfoUseCase(
    private val connectionRepository: Repositories.Connection,
    private val pragmaRepository: Repositories.Pragma
) : UseCases.GetTableInfo {

    override suspend fun invoke(input: Query): Page {
        val connection = connectionRepository.open(input.databasePath)
        val tableInfo = pragmaRepository.getTableInfo(
            input.copy(
                database = connection
            )
        )
        return tableInfo.copy(
            fields = tableInfo.fields.filterIndexed { index, _ ->
                index % TableInfoColumns.values().size == TableInfoColumns.NAME.ordinal
            }
        )
    }
}