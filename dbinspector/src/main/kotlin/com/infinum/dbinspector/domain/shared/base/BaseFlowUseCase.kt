package com.infinum.dbinspector.domain.shared.base

internal interface BaseFlowUseCase<InputModel : BaseParameters, OutputModel> {

    operator fun invoke(input: InputModel): OutputModel = throw NotImplementedError()
}
