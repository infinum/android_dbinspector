package com.infinum.dbinspector.domain.settings.usecases

import android.text.TextUtils
import com.infinum.dbinspector.domain.Repositories
import com.infinum.dbinspector.domain.UseCases

internal class SaveTruncateModeUseCase(
    private val settingsRepository: Repositories.Settings
) : UseCases.SaveTruncateMode {

    override suspend fun invoke(input: TextUtils.TruncateAt) =
        settingsRepository.saveTruncateMode(input)
}
