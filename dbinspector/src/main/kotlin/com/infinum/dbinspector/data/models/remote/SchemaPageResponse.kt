package com.infinum.dbinspector.data.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SchemaPageResponse(

    @SerialName("nextPage")
    val nextPage: Int? = null,

    @SerialName("beforeCount")
    val beforeCount: Int = 0,

    @SerialName("afterCount")
    val afterCount: Int = 0,

    @SerialName("cells")
    val cells: List<SchemaResponse>
)