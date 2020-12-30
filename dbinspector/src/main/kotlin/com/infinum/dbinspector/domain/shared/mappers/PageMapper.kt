package com.infinum.dbinspector.domain.shared.mappers

import com.infinum.dbinspector.data.models.local.cursor.input.Order
import com.infinum.dbinspector.data.models.local.cursor.output.QueryResult
import com.infinum.dbinspector.data.models.local.proto.SettingsEntity
import com.infinum.dbinspector.domain.Mappers
import com.infinum.dbinspector.domain.shared.base.BaseMapper
import com.infinum.dbinspector.domain.shared.models.BlobPreviewMode
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.Sort

internal class PageMapper(
    private val cellMapper: Mappers.Cell,
    private val sortMapper: Mappers.Sort,
    private val blobPreviewModeMapper: Mappers.BlobPreviewMode
) : Mappers.Page {

    override fun sortMapper(): BaseMapper<Order, Sort> =
        sortMapper

    override fun blobPreviewModeMapper(): BaseMapper<SettingsEntity.BlobPreviewMode, BlobPreviewMode> =
        blobPreviewModeMapper

    override fun mapLocalToDomain(model: QueryResult): Page =
        Page(
            beforeCount = model.beforeCount,
            afterCount = model.afterCount,
            cells = model.rows.map { row ->
                row.fields.toList().map { field -> cellMapper.mapLocalToDomain(field) }
            }.flatten(),
            nextPage = model.nextPage
        )
}
