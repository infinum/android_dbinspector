package com.infinum.dbinspector.data.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RowResponse(

    @SerialName("cells")
    val cells: List<String>? = null
)
