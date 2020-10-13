package im.dino.dbinspector.data.models.local

internal data class QueryResult(
    val rows: List<Row>,
    val nextPage: Int? = null
)
