package com.infinum.dbinspector.ui.pragma.shared

import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.PragmaParameters
import com.infinum.dbinspector.ui.shared.datasources.PageDataSource

internal class PragmaDataSource(
    databasePath: String,
    statement: String,
    useCase: BaseUseCase<PragmaParameters.Pragma, Page>
) : PageDataSource<PragmaParameters.Pragma>(useCase) {

    override var parameters = PragmaParameters.Pragma(
        databasePath = databasePath,
        statement = statement
    )
}
