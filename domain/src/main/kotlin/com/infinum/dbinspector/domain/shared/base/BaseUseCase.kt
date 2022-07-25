package com.infinum.dbinspector.domain.shared.base

public interface BaseUseCase<InputModel : BaseParameters, OutputModel> {

    public suspend operator fun invoke(input: InputModel): OutputModel = throw NotImplementedError()
}
