package com.infinum.dbinspector.data.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SchemaResponse(

    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null
)
