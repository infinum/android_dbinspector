package com.infinum.dbinspector.domain.raw

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters

internal class RawRepository(
    private val getPageInteractor: Interactors.GetRawQuery,
    private val getHeadersInteractor: Interactors.GetRawQueryHeaders,
    private val getAffectedRowsInteractor: Interactors.GetAffectedRows,
    private val control: Control.Content
) : Repositories.RawQuery {

    override suspend fun getPage(input: ContentParameters): Page =
        control.mapper(getPageInteractor(control.converter(input)))

    override suspend fun getHeaders(input: ContentParameters): Page =
        control.mapper(getHeadersInteractor(control.converter(input)))

    override suspend fun getAffectedRows(input: ContentParameters): Page =
        control.mapper(getAffectedRowsInteractor(control.converter(input)))
}
