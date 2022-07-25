package com.infinum.dbinspector.data.models.local.cursor.output

public data class QueryResult(
    val rows: List<Row>,
    val nextPage: Int? = null,
    val beforeCount: Int = 0,
    val afterCount: Int = 0
)
