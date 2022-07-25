package com.infinum.dbinspector.domain.shared.base

public interface BaseFlowUseCase<InputModel : BaseParameters, OutputModel> {

    public operator fun invoke(input: InputModel): OutputModel = throw NotImplementedError()
}
