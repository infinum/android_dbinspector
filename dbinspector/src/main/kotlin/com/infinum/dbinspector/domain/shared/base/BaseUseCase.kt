package com.infinum.dbinspector.domain.shared.base

internal interface BaseUseCase<InputModel : BaseParameters, OutputModel> {

    suspend operator fun invoke(input: InputModel): OutputModel
}
