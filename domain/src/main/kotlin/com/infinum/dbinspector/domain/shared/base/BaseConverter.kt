package com.infinum.dbinspector.domain.shared.base

internal interface BaseConverter<Input : BaseParameters, Output> {

    suspend operator fun invoke(parameters: Input): Output = throw NotImplementedError()
}
