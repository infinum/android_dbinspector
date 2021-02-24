package com.infinum.dbinspector.domain.shared.base

internal interface BaseFlowInteractor<InputModel, OutputModel> {

    operator fun invoke(input: InputModel): OutputModel = throw NotImplementedError()
}
