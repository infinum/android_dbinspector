package com.infinum.dbinspector.domain.schema.control.converters

import com.infinum.dbinspector.data.models.local.cursor.input.Query
import com.infinum.dbinspector.domain.Converters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class SchemaConverter(
    private val sortConverter: Converters.Sort,
    private val blobPreviewConverter: Converters.BlobPreview
) : Converters.Schema {

    override suspend fun getPage(parameters: ContentParameters): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.database,
            statement = parameters.statement,
            order = sortConverter(parameters.sort),
            pageSize = parameters.pageSize,
            page = parameters.page,
            blobPreview = blobPreviewConverter(
                SettingsParameters.BlobPreview(parameters.blobPreviewMode)
            )
        )

    override suspend fun getByName(parameters: ContentParameters): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.database,
            statement = parameters.statement,
            order = sortConverter(parameters.sort),
            pageSize = parameters.pageSize,
            page = parameters.page,
            blobPreview = blobPreviewConverter(
                SettingsParameters.BlobPreview(parameters.blobPreviewMode)
            )
        )

    override suspend fun dropByName(parameters: ContentParameters): Query =
        Query(
            databasePath = parameters.databasePath,
            database = parameters.database,
            statement = parameters.statement,
            order = sortConverter(parameters.sort),
            pageSize = parameters.pageSize,
            page = parameters.page,
            blobPreview = blobPreviewConverter(
                SettingsParameters.BlobPreview(parameters.blobPreviewMode)
            )
        )
}
