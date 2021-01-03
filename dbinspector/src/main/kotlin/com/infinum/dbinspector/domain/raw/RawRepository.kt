package com.infinum.dbinspector.domain.raw

import com.infinum.dbinspector.domain.Control
import com.infinum.dbinspector.domain.Interactors
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters

internal class RawRepository(
    private val getPageInteractor: Interactors.GetRawQuery,
    private val control: Control.RawQuery
) : Repositories.RawQuery {

    override suspend fun getPage(input: ContentParameters): Page =
        control.mapper(getPageInteractor(control.converter(input)))
}
