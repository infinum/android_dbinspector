package com.infinum.dbinspector.data.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PageResponse(

    @SerialName("nextPage")
    val nextPage: Int? = null,

    @SerialName("beforeCount")
    val beforeCount: Int = 0,

    @SerialName("afterCount")
    val afterCount: Int = 0,

    @SerialName("columns")
    val columns: List<String>,

    @SerialName("rows")
    val rows: List<RowResponse>
)