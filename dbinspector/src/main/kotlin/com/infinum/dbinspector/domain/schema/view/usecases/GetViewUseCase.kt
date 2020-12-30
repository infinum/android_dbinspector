package com.infinum.dbinspector.domain.schema.view.usecases

import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases
import com.infinum.dbinspector.domain.shared.models.Page
import com.infinum.dbinspector.domain.shared.models.parameters.ConnectionParameters
import com.infinum.dbinspector.domain.shared.models.parameters.ContentParameters
import com.infinum.dbinspector.domain.shared.models.parameters.SettingsParameters

internal class GetViewUseCase(
    private val connectionRepository: Repositories.Connection,
    private val settingsRepository: Repositories.Settings,
    private val schemaRepository: Repositories.Schema
) : UseCases.GetView {

    override suspend fun invoke(input: ContentParameters): Page {
        val connection = connectionRepository.open(ConnectionParameters(input.databasePath))
        val settings = settingsRepository.getPage(SettingsParameters.Get())
        return schemaRepository.getByName(
            input.copy(
                database = connection.database,
                blobPreviewMode = settings.blobPreviewMode
            )
        )
    }
}
