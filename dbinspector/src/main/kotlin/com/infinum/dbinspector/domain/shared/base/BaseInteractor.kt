package com.infinum.dbinspector.domain.shared.base

internal interface BaseInteractor<InputModel, OutputModel> {

    suspend operator fun invoke(input: InputModel): OutputModel
}
