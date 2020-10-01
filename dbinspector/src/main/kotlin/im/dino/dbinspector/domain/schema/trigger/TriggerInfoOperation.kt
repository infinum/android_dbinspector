package im.dino.dbinspector.domain.schema.trigger

import im.dino.dbinspector.data.models.Row
import im.dino.dbinspector.domain.schema.models.TriggerColumns
import im.dino.dbinspector.domain.shared.AbstractSchemaOperation
import java.util.Locale

internal class TriggerInfoOperation : AbstractSchemaOperation<List<Row>>() {

    override fun query(): String = ""

    override fun invoke(path: String, nextPage: Int?): List<Row> {
        return TriggerColumns.values().map {
            Row(
                0,
                listOf(it.name.toLowerCase(Locale.getDefault()))
            )
        }
    }
}