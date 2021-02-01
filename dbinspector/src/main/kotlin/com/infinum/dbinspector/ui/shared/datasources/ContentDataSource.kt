package com.infinum.dbinspector.ui.shared.datasources

import com.infinum.dbinspector.domain.shared.base.BaseUseCase
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters

internal class ContentDataSource(
    databasePath: String,
    statement: String,
    useCase: BaseUseCase<ContentParameters, Page>
) : PageDataSource<ContentParameters>(useCase) {

    override var parameters: ContentParameters = ContentParameters(
        databasePath = databasePath,
        statement = statement
    )
}
