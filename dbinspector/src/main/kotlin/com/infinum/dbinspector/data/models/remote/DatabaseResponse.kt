package com.infinum.dbinspector.data.models.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DatabaseResponse(

    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("path")
    val path: String,

    @SerialName("version")
    val version: String
)